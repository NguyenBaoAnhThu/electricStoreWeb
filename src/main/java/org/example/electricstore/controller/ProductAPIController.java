package org.example.electricstore.controller;

import org.example.electricstore.DTO.order.ProductChosen;
import org.example.electricstore.model.Product;
import org.example.electricstore.service.impl.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductAPIController {

    @Autowired
    private ProductService productService;

    @GetMapping("/productChosen")
    public List<ProductChosen> getProductById(
            @RequestParam(value = "oldData", defaultValue = "") String listId,
            @RequestParam(value = "newData", defaultValue = "0") Integer id) {

        List<ProductChosen> products = new ArrayList<>();

        if(!listId.equals("empty")){
            List<Integer> productIds = new ArrayList<>();
            List<Integer> quantityList = new ArrayList<>();
            String[] listIdArr = listId.split("-");
            for (String idOld : listIdArr) {
                String[] idArr = idOld.split("\\.");
                Integer productId = Integer.parseInt(idArr[0]);
                Integer quantity = Integer.parseInt(idArr[1]);
                productIds.add(productId);
                quantityList.add(quantity);
            }

            for (int i = 0; i < productIds.size(); i++) {
                ProductChosen product = productService.getProductByIdUseInOrder(productIds.get(i));
                product.setQuantity(quantityList.get(i)>0?quantityList.get(i):1);
                products.add(product);
            }
            Boolean isExist = false;
            for(int j = 0; j< productIds.size(); j++){
                if(productIds.get(j) == id){
                    isExist = true;
                    break;
                }
            }
            if(!isExist&&id!=0){
                ProductChosen product = productService.getProductByIdUseInOrder(id);
                product.setQuantity(1);
                products.add(product);
            }
        }else {
            ProductChosen product = productService.getProductByIdUseInOrder(id);
            product.setQuantity(1);
            products.add(product);
        }

        return products;
    }

    /**
     * API để lấy tồn kho hiện tại của một sản phẩm
     * @param productId ID của sản phẩm
     * @return Thông tin tồn kho
     */
    @GetMapping("/stock/{productId}")
    public ResponseEntity<Map<String, Object>> getCurrentStock(@PathVariable Integer productId) {
        try {
            Product product = productService.findById(productId);

            if (product == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Không tìm thấy sản phẩm với ID: " + productId);
                return ResponseEntity.notFound().build();
            }

            Map<String, Object> response = new HashMap<>();
            response.put("productId", productId);
            response.put("productName", product.getName());
            response.put("stock", product.getStock() != null ? product.getStock() : 0);
            response.put("price", product.getPrice() != null ? product.getPrice() : 0);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Không thể lấy thông tin tồn kho: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * API để lấy tồn kho của nhiều sản phẩm cùng lúc
     * @param productIds Danh sách ID sản phẩm
     * @return Map chứa tồn kho của từng sản phẩm
     */
    @PostMapping("/stock/batch")
    public ResponseEntity<Map<String, Object>> getBatchStock(@RequestBody List<Integer> productIds) {
        try {
            Map<String, Object> response = new HashMap<>();
            Map<Integer, Map<String, Object>> stockMap = new HashMap<>();

            for (Integer productId : productIds) {
                try {
                    Product product = productService.findById(productId);
                    Map<String, Object> productInfo = new HashMap<>();

                    if (product != null) {
                        productInfo.put("productId", productId);
                        productInfo.put("productName", product.getName());
                        productInfo.put("stock", product.getStock() != null ? product.getStock() : 0);
                        productInfo.put("price", product.getPrice() != null ? product.getPrice() : 0);
                        productInfo.put("exists", true);
                    } else {
                        productInfo.put("productId", productId);
                        productInfo.put("productName", "Sản phẩm không tồn tại");
                        productInfo.put("stock", 0);
                        productInfo.put("price", 0);
                        productInfo.put("exists", false);
                    }

                    stockMap.put(productId, productInfo);
                } catch (Exception e) {
                    Map<String, Object> productInfo = new HashMap<>();
                    productInfo.put("productId", productId);
                    productInfo.put("error", "Lỗi khi lấy thông tin: " + e.getMessage());
                    productInfo.put("stock", 0);
                    productInfo.put("exists", false);
                    stockMap.put(productId, productInfo);
                }
            }

            response.put("stocks", stockMap);
            response.put("success", true);
            response.put("totalProducts", productIds.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Không thể lấy thông tin tồn kho batch: " + e.getMessage());
            error.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * API để kiểm tra tồn kho có đủ cho một đơn hàng không
     * @param request Danh sách sản phẩm và số lượng cần kiểm tra
     * @return Kết quả kiểm tra tồn kho
     */
    @PostMapping("/stock/validate")
    public ResponseEntity<Map<String, Object>> validateStock(
            @RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> items = (List<Map<String, Object>>) request.get("items");

            Map<String, Object> response = new HashMap<>();
            List<Map<String, Object>> invalidItems = new ArrayList<>();
            boolean isValid = true;

            for (Map<String, Object> item : items) {
                Integer productId = (Integer) item.get("productId");
                Integer requestedQuantity = (Integer) item.get("quantity");

                try {
                    Product product = productService.findById(productId);

                    if (product == null) {
                        Map<String, Object> invalidItem = new HashMap<>();
                        invalidItem.put("productId", productId);
                        invalidItem.put("error", "Sản phẩm không tồn tại");
                        invalidItem.put("requestedQuantity", requestedQuantity);
                        invalidItem.put("availableStock", 0);
                        invalidItems.add(invalidItem);
                        isValid = false;
                        continue;
                    }

                    Integer availableStock = product.getStock() != null ? product.getStock() : 0;

                    if (requestedQuantity > availableStock) {
                        Map<String, Object> invalidItem = new HashMap<>();
                        invalidItem.put("productId", productId);
                        invalidItem.put("productName", product.getName());
                        invalidItem.put("requestedQuantity", requestedQuantity);
                        invalidItem.put("availableStock", availableStock);
                        invalidItem.put("error", "Không đủ tồn kho");
                        invalidItems.add(invalidItem);
                        isValid = false;
                    }
                } catch (Exception e) {
                    Map<String, Object> invalidItem = new HashMap<>();
                    invalidItem.put("productId", productId);
                    invalidItem.put("error", "Lỗi khi kiểm tra: " + e.getMessage());
                    invalidItem.put("requestedQuantity", requestedQuantity);
                    invalidItems.add(invalidItem);
                    isValid = false;
                }
            }

            response.put("isValid", isValid);
            response.put("invalidItems", invalidItems);
            response.put("totalChecked", items.size());
            response.put("totalInvalid", invalidItems.size());

            if (!isValid) {
                return ResponseEntity.badRequest().body(response);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Không thể kiểm tra tồn kho: " + e.getMessage());
            error.put("isValid", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * API để lấy thông tin chi tiết sản phẩm (bao gồm tồn kho)
     * @param productId ID sản phẩm
     * @return Thông tin chi tiết sản phẩm
     */
    @GetMapping("/details/{productId}")
    public ResponseEntity<Map<String, Object>> getProductDetails(@PathVariable Integer productId) {
        try {
            Product product = productService.findById(productId);

            if (product == null) {
                return ResponseEntity.notFound().build();
            }

            Map<String, Object> response = new HashMap<>();
            response.put("productId", product.getProductID());
            response.put("productName", product.getName());
            response.put("description", product.getDescription());
            response.put("price", product.getPrice() != null ? product.getPrice() : 0);
            response.put("stock", product.getStock() != null ? product.getStock() : 0);
            response.put("categoryId", product.getCategory() != null ? product.getCategory().getCategoryID() : null);
            response.put("categoryName", product.getCategory() != null ? product.getCategory().getCategoryName() : null);
            response.put("brandId", product.getBrand() != null ? product.getBrand().getBrandID() : null);
            response.put("brandName", product.getBrand() != null ? product.getBrand().getBrandName() : null);
            response.put("isAvailable", product.getStock() != null && product.getStock() > 0);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Không thể lấy thông tin sản phẩm: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}