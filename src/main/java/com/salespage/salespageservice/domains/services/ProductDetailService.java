package com.salespage.salespageservice.domains.services;

import com.salespage.salespageservice.app.dtos.productDtos.ProductDetailDto;
import com.salespage.salespageservice.app.responses.ProductResponse.ProductDetailInfoResponse;
import com.salespage.salespageservice.app.responses.UploadImageData;
import com.salespage.salespageservice.domains.entities.Product;
import com.salespage.salespageservice.domains.entities.ProductDetail;
import com.salespage.salespageservice.domains.exceptions.ResourceNotFoundException;
import com.salespage.salespageservice.domains.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductDetailService extends BaseService{

  public List<ProductDetailInfoResponse> getProductDetail(String username, String productId) {
    List<ProductDetail> productDetails = productDetailStorage.findByProductId(productId);
    return modelMapper.toListProductDetailInfo(productDetails);
  }

  public void createProductDetail(String username, ProductDetailDto dto) {
    Product product = productStorage.findProductById(dto.getProductId());
    if(Objects.isNull(product)){
      throw new ResourceNotFoundException("Không tồn tại sản phẩm");
    }
    ProductDetail productDetail = modelMapper.toProductDetail(dto);
    productDetail.setSellPrice(productDetail.getOriginPrice() - productDetail.getOriginPrice() * (productDetail.getDiscountPercent()/100));

    productDetailStorage.save(productDetail);
  }

  public void updateProductDetail(String username, String detailId, ProductDetailDto dto) {
    ProductDetail productDetail = productDetailStorage.findById(detailId);
    if(Objects.isNull(productDetail)){
      throw new ResourceNotFoundException("Không tồn tại chi tiết sản phẩm");
    }
    modelMapper.mapToProductDetailDto(dto, productDetail);
    productDetail.setSellPrice(productDetail.getOriginPrice() - productDetail.getOriginPrice() * (productDetail.getDiscountPercent()/100));
    productDetailStorage.save(productDetail);
  }

  public void deleteProductDetail(String username, String detailId){
    ProductDetail productDetail = productDetailStorage.findById(detailId);
    if(Objects.isNull(productDetail)){
      throw new ResourceNotFoundException("Không tồn tại chi tiết sản phẩm");
    }
    productDetailStorage.delete(productDetail);
  }

  public UploadImageData uploadImageUrl(String username, String id, MultipartFile file) throws IOException {
    String imageUrl;
    String folderName = "salepage-" + username + "-productDetail-" + id;
    ProductDetail productDetail = productDetailStorage.findById(id);
    if(productDetail == null){
      throw new ResourceNotFoundException("Không tìm thấy sản phẩm");
    }else{
      imageUrl = driver.uploadPublicImage(folderName, id, Helper.convertMultiPartToFile(file));
      productDetail.setImageUrl(imageUrl);
      productDetailStorage.save(productDetail);
      driver.deleteFolderByName(folderName);
    }

    return new UploadImageData(imageUrl);
  }
}
