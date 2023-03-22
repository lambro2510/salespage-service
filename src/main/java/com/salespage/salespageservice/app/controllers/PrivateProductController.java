@CrossOrigin
@RestController
@RequestMapping("v1/api/product")
@SecurityRequirement(name = "bearerAuth")
public class PrivateProductController extends BaseController {

    @Autowired
    private ProductService productService;

    @PostMapping("")
    @Operation(summary = "Create a product", description = "Create a new product with the given information")
    @ApiResponse(responseCode = "201", description = "Product created successfully")
    public ResponseEntity<Product> createProduct(Authentication authentication, @RequestBody ProductInfoDto dto) {
        return productService.createProduct(getUsername(authentication), dto);
    }

    @PostMapping("upload-images")
    @Operation(summary = "Upload images for a product", description = "Upload one or more images for a product with the given ID")
    @ApiResponse(responseCode = "200", description = "Images uploaded successfully")
    public ResponseEntity<List<String>> uploadImages(Authentication authentication, @RequestParam String productId, @RequestParam List<MultipartFile> files) throws IOException {
        return productService.uploadProductImage(getUsername(authentication), productId, files);
    }

    @DeleteMapping("delete-images")
    @Operation(summary = "Delete images for a product", description = "Delete one or more images for a product with the given ID")
    @ApiResponse(responseCode = "200", description = "Images deleted successfully")
    public ResponseEntity<List<String>> deleteImages(Authentication authentication, @RequestParam String productId, @RequestBody List<String> imageUrls) throws IOException {
        return productService.deleteProductImages(getUsername(authentication), productId, imageUrls);
    }

    @DeleteMapping("")
    @Operation(summary = "Delete a product", description = "Delete a product with the given ID")
    @ApiResponse(responseCode = "200", description = "Product deleted successfully")
    public ResponseEntity<Boolean> deleteProduct(Authentication authentication, @RequestParam String productId) throws IOException {
        return productService.deleteProduct(getUsername(authentication), productId);
    }

    @PutMapping("")
    @Operation(summary = "Update a product", description = "Update a product with the given information")
    @ApiResponse(responseCode = "200", description = "Product updated successfully")
    public ResponseEntity<Product> updateProduct(Authentication authentication, @RequestBody ProductDto dto) {
        return productService.updateProduct(getUsername(authentication), dto);
    }

}
