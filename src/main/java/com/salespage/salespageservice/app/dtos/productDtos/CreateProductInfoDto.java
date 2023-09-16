package com.salespage.salespageservice.app.dtos.productDtos;

import com.salespage.salespageservice.domains.entities.types.SizeType;
import com.salespage.salespageservice.domains.entities.types.WeightType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.*;
import java.util.List;

@Data
public class CreateProductInfoDto {

  @NotBlank(message = "Tên sản phẩm không được để trống")
  @Size(max = 255, message = "Tên sản phẩm tối đa 255 ký tự")
  @Schema(description = "Tên sản phẩm", example = "Điện thoại iPhone 13")
  private String productName;

  @NotBlank(message = "Mô tả không được để trống")
  @Schema(description = "Mô tả sản phẩm", example = "Điện thoại iPhone 13 với thiết kế mới, camera đột phá và hiệu suất tuyệt vời")
  private String description;

  @NotNull(message = "Loại sản phẩm không được để trống")
  @Schema(description = "ID loại sản phẩm", example = "64c3f378baa11809a48e6cab")
  private String categoryId;

  @NotNull(message = "Giá sản phẩm không được để trống")
  @DecimalMin(value = "0.0", inclusive = false, message = "Giá sản phẩm phải lớn hơn 0")
  @Schema(description = "Giá sản phẩm", example = "20990000")
  private Double productPrice;

  @Size(max = 30, message = "Id cửa hàng tối đa 30 ký tự")
  @Schema(description = "Danh sách ID cửa hàng", example = "[\"642835ac24d1d851192a251d\",\"6428636624d1d851192a251e\",\"645c82f65ccca035f58f790e\"]")
  private List<String> storeIds;

  @Schema(description = "Xuất xứ sản phẩm", example = "Mỹ")
  private String origin;

  @Schema(description = "Sản phẩm là nhập khẩu hay không", example = "true")
  private Boolean isForeign;

  @Schema(description = "Kích thước sản phẩm", example = "5")
  private Long size;

  @Schema(description = "Đơn vị kích thước (ví dụ: cm)", example = "CENTIMES")
  private SizeType sizeType;

  @Schema(description = "Trọng lượng sản phẩm", example = "500")
  private Long weight;

  @Schema(description = "Đơn vị trọng lượng (ví dụ: gram)", example = "GRAM")
  private WeightType weightType;

  @Schema(description = "Danh sách màu sắc sản phẩm", example = "[\"đen\", \"trắng\", \"xanh\"]")
  private List<String> colors;

  @Schema(description = "Có chế độ bảo hành không", example = "true")
  private Boolean isGuarantee;

  @Schema(description = "Số lượng", example = "5")
  private Long quantity;

  @Schema(description = "Phần trăm giảm giá", example = "5")
  @Min(0)
  @Max(100)
  private Double discountPercent;
}

