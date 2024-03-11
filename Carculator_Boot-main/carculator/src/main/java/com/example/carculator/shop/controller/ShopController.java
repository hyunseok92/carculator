package com.example.carculator.shop.controller;

import com.example.carculator.shop.entity.Bill;
import com.example.carculator.shop.entity.Cart;
import com.example.carculator.shop.entity.Product;
import com.example.carculator.shop.model.ShopService;
import com.example.carculator.shop.model.dto.BillDetailDto;
import com.example.carculator.shop.model.dto.BillDto;
import com.example.carculator.shop.model.dto.CartDto;
import com.example.carculator.shop.model.dto.ProductDto;
import com.example.carculator.commons.FileUplode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@CrossOrigin
@RestController
public class ShopController {

    private final ShopService shopService;


    @PostMapping(value = "/shop/shopAdminInsert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Product insertShop(@ModelAttribute Product product, @RequestPart("pimg") MultipartFile pimg,
                              @RequestPart("dimg") MultipartFile dimg
    ) {
        log.info(product.toString());
        String uploadPath = System.getProperty("user.dir") +"\\carculator\\src\\main\\resources\\static\\productImages\\";
        try {

            if (pimg != null) {
                String pimgName = pimg.getOriginalFilename();
                product.setProductImg(pimgName);
                product.setProductRmg(FileUplode.uploadFile(uploadPath, pimgName, pimg));
            }
            if (dimg != null) {
                String dimgName = dimg.getOriginalFilename();
                product.setDetailImg(dimgName);
                product.setDetailRmg(FileUplode.uploadFile(uploadPath, dimgName, dimg));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info(product.toString());
        return shopService.productWrite(product);
    }

    @GetMapping("/shop/shopAdminList")
    public List<ProductDto> getShopAdminList() {
        return shopService.getShopAdminList();
    }

    @DeleteMapping("/shop/shopAdminList/{productCode}")
    public List<ProductDto> deleteShopAdminList(@PathVariable("productCode") Long productCode) {
        String deletePath = System.getProperty("user.dir") + "\\carculator\\src\\main\\resources\\static\\productImages\\";
        log.info(productCode.toString());
        ProductDto pdto = shopService.selectone(productCode);
        if (pdto.getProductRmg() != null) {
            Path del1 = Paths.get(deletePath + pdto.getProductRmg());
            try {
                Files.delete(del1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (pdto.getDetailRmg() != null) {
            Path del2 = Paths.get(deletePath + pdto.getDetailRmg());
            try {
                Files.delete(del2);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        shopService.deleteShopAdminList(productCode);
        return shopService.getShopAdminList();

    }

    @GetMapping("/shop/shopAdminList/update/{productCode}")
    public ProductDto updateShopAdminList(@PathVariable("productCode") Long productCode) {
        log.info(productCode.toString());
        return shopService.selectone(productCode);
    }

    @PatchMapping(value = "/shop/shopAdminList/update/{productCode}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<ProductDto> udpateProduct(@ModelAttribute Product product, @RequestPart(name = "pimg", required = false) MultipartFile pimg,
                                          @RequestPart(name = "dimg", required = false) MultipartFile dimg) throws Exception {
        log.info(product.toString());
        ProductDto product2 = shopService.selectone(product.getProductCode());
        product.setProductImg(product2.getProductImg());
        product.setProductRmg(product2.getProductRmg());
        product.setDetailImg(product2.getDetailImg());
        product.setDetailRmg(product2.getDetailRmg());
        String uploadPath = System.getProperty("user.dir") + "\\carculator\\src\\main\\resources\\static\\productImages\\";
        if (pimg != null) {
            Path del1 = Paths.get(uploadPath + product.getProductRmg());
            try {
                Files.delete(del1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String pimgName = pimg.getOriginalFilename();
            product.setProductImg(pimgName);
            product.setProductRmg(FileUplode.uploadFile(uploadPath, pimgName, pimg));
        }
        if (dimg != null) {
            Path del2 = Paths.get(uploadPath + product.getDetailRmg());
            try {
                Files.delete(del2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String dimgName = dimg.getOriginalFilename();
            product.setDetailImg(dimgName);
            product.setDetailRmg(FileUplode.uploadFile(uploadPath, dimgName, dimg));
        }
        shopService.updateProduct(product);
        return shopService.getShopAdminList();


    }

    @GetMapping("/shop/product/{productCode}")

    public ProductDto selectone(@PathVariable("productCode") Long productCode) {

        ProductDto productDto = shopService.selectone(productCode);
        if (productDto.getProductView() == null) {
            productDto.setProductView(1L);
            log.info("productView : " + productDto.getProductView());
        } else {
            productDto.setProductView((productDto.getProductView() + 1L));
            log.info("productView : " + productDto.getProductView());
        }
        shopService.updateProductCount(productDto);
        return shopService.selectone(productCode);
    }

    @DeleteMapping("/shop/product/payment/{billNo}")
    public void deletePayment(@PathVariable("billNo") String billNo) {
        if(shopService.findBill(billNo) != null){
            shopService.deleteBillDetail(billNo);
        }
        shopService.deletePayment(billNo);
    }

    @PatchMapping("/shop/success/")
    public void successPayment(@RequestBody Bill bill) {
        log.info(bill.toString());
        BillDto bill2 = shopService.findBill(bill.getBillNo());
        if (Objects.equals(bill2.getBillAmount(), bill.getBillAmount()) && Objects.equals(bill2.getBillNo(), bill.getBillNo())) {
            shopService.successPayment(bill);

        } else {
            shopService.deletePayment(bill.getBillNo());
            shopService.deleteBillDetail(bill.getBillNo());
        }
    }

    @PostMapping("/shop/product/payment/")
    public ResponseEntity<?> processPayment(@RequestBody Map<String, Object> payload) {
        Map<String, Object> bill = (Map<String, Object>) payload.get("bill");
        List<Map<String, Object>> products = (List<Map<String, Object>>) payload.get("products");
        Object billCount = payload.get("billCount");
        Long billCount2 = null;
        List billCount3 = null;
        if(billCount instanceof  List){
            billCount3 = (List) billCount;
        }else{
            billCount2 = Long.parseLong(billCount.toString());
        }


        log.info(billCount.toString());
        Bill bill2 = new Bill();
        bill2.setBillNo((String) bill.get("billNo"));
        bill2.setBillAmount(Long.parseLong((String)bill.get("billAmount")));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        String dateString = (String) bill.get("billDate");
        String formattedDateString = dateString.substring(0, dateString.length() - 1);
        LocalDateTime parsedDateTime = LocalDateTime.parse(formattedDateString, formatter);
        bill2.setBillDate(parsedDateTime);
        bill2.setBillEmail((String) bill.get("billEmail"));
        bill2.setBillName((String) bill.get("billName"));
        bill2.setBillPhone((String) bill.get("billPhone"));
        bill2.setBillAddress((String) bill.get("billAddress"));
        bill2.setBillAddressNo((String) bill.get("billAddressNo"));
        bill2.setBillRequest((String) bill.get("billRequest"));
        bill2.setMemberCode((String) bill.get("memberCode"));
        bill2.setPayName((String) bill.get("payName"));
        bill2.setRecipient((String) bill.get("recipient"));
        bill2.setBillKind((String) bill.get("billKind"));
        shopService.insertPayment(bill2);
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            Product product = new Product();
            product.setProductCode(Long.parseLong(products.get(i).get("productCode").toString()));
            if(billCount instanceof  List){
                product.setProductCount((Long.parseLong(products.get(i).get("productCount").toString()) - Long.parseLong(billCount3.get(i).toString())));
                if(products.get(i).get("productSum") == null){
                    product.setProductSum(Long.parseLong(billCount3.get(i).toString()));
                }else {
                    product.setProductSum(Long.parseLong(products.get(i).get("productSum").toString()) + Long.parseLong(billCount3.get(i).toString()));
                }
            }else{
                product.setProductCount((Long.parseLong(products.get(i).get("productCount").toString()) - billCount2));
                if(products.get(i).get("productSum") == null){
                    product.setProductSum(billCount2);
                }else {
                    product.setProductSum(Long.parseLong(products.get(i).get("productSum").toString()) + billCount2);
                }
            }


            shopService.updateProductCount(product.getProductCode(), product.getProductCount(), product.getProductSum());
        }
        List<BillDetailDto> billDetailList = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            BillDetailDto billDetail = new BillDetailDto();
            billDetail.setBillNo((String) bill.get("billNo"));
            billDetail.setProductCode(Long.parseLong(products.get(i).get("productCode").toString()));
            if(billCount instanceof  List){
                billDetail.setBillCount(Long.parseLong(billCount3.get(i).toString()));
            }else{
                billCount2 = Long.parseLong(billCount.toString());
                billDetail.setBillCount(billCount2);
            }

            billDetail.setMemberCode((String) bill.get("memberCode"));
            billDetail.setProductAmount(Long.parseLong(products.get(i).get("productAmount").toString()));
            billDetail.setProductName((String) products.get(i).get("productName"));
            log.info(billDetail.toString());
            shopService.insertBillDetail(billDetail);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/shop/addCart")
    public ResponseEntity<?> addCart(@RequestBody Cart cart) {
        cart.setCartCode(cart.getProductCode() + cart.getMemberCode());
        log.info(cart.toString());
        if(cart.getBillCount() == null){
            cart.setBillCount(1L);
        }
        ProductDto product = shopService.selectone(cart.getProductCode());
        cart.setProductAmount(product.getProductAmount());
        cart.setProductName(product.getProductName());
        cart.setProductRmg(product.getProductRmg());
        cart.setProductCount(product.getProductCount());
        if(shopService.findCart(cart) != null){
            CartDto cart2 = shopService.findCart(cart);
            cart.setBillCount(cart.getBillCount() + cart2.getBillCount());
        }
        shopService.addCart(cart);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/shop/cartList/{memberCode}")
    public List<CartDto> getCartList(@PathVariable("memberCode") String memberCode) {
        return shopService.getCartList(memberCode);
    }

    @DeleteMapping("/shop/cartList/{cartCode}")
    public List<CartDto> deleteCartList(@PathVariable("cartCode") String cartCode,@RequestBody Map<String, String> body) {
        shopService.deleteCartList(cartCode);
        String memberCode = body.get("memberCode");
        return shopService.getCartList(memberCode);
    }

    @GetMapping("/shop/paymentResult/")
    public List<BillDto> getPaymentResult(){
        return shopService.getPaymentResult();
    }

    @GetMapping("/shop/paymentResult/{billNo}")
    public List<BillDetailDto> getPaymentResultDetail(@PathVariable("billNo") String billNo){
        return shopService.getPaymentResultDetail(billNo);
    }

    @PutMapping("/shop/paymentResult/{billNo}")
    public List<BillDetailDto> updateInvNo(@PathVariable("billNo") String billNo, @RequestBody Map<String, Object> payload) {
        String invNo = payload.get("invNo").toString();
        shopService.updateInvNo(billNo, invNo);
        return shopService.getPaymentResultDetail(billNo);
    }

    @GetMapping("/shop/paymentUser/{memberCode}")
    public List<BillDto> getPaymentUser(@PathVariable("memberCode") String memberCode){
        return shopService.getPaymentUser(memberCode);
    }
    @PutMapping("/shop/paymentUser/{billNo}")
    public List<BillDetailDto> updateCancel(@PathVariable("billNo") String billNo, @RequestBody Map<String, Object> payload) {
        String billCancel = payload.get("billCancle").toString();
        shopService.updateCancel(billNo, billCancel);
        return shopService.getPaymentResultDetail(billNo);
    }
    @PutMapping("/shop/paymentCancel/{billNo}")
    public List<BillDetailDto> paymentCancel(@PathVariable("billNo") String billNo) {
        shopService.paymentCancel(billNo);
        return shopService.getPaymentResultDetail(billNo);
    }

    @PutMapping("/shop/paymentUserDeliOk/{billNo}")
    public List<BillDetailDto> updateDeliOk(@PathVariable("billNo") String billNo) {
        shopService.updateDeliOk(billNo);
        return shopService.getPaymentResultDetail(billNo);
    }





}
