package com.example.carculator.shop.model;

import com.example.carculator.shop.entity.Bill;
import com.example.carculator.shop.entity.BillDetail;
import com.example.carculator.shop.entity.Cart;
import com.example.carculator.shop.entity.Product;
import com.example.carculator.shop.model.dto.BillDetailDto;
import com.example.carculator.shop.model.dto.BillDto;
import com.example.carculator.shop.model.dto.CartDto;
import com.example.carculator.shop.model.dto.ProductDto;
import com.example.carculator.shop.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ShopService {

    private final ProductRepository productRepository;
    private final ProductRepositoryCustom productRepositoryCustom;
    private final BillRepository billRepository;
    private final BillRepositoryCustom billRepositoryCustom;
    private final BillDetailRepository billDetailRepository;
    private final BillDetailRepositoryCustom billDetailRepositoryCustom;
    private final CartRepository cartRepository;
    private final CartRepositoryCustom cartRepositoryCustom;
    private final SumCountRepository sumCountRepository;
    private final SumCountRepositoryCustom sumCountRepositoryCustom;

    @Transactional
    public Product productWrite(Product product){
        Product entity = Product.builder()
                .productName(product.getProductName())
                .category1(product.getCategory1())
                .productCount(product.getProductCount())
                .productAmount(product.getProductAmount())
                .detailImg(product.getDetailImg())
                .detailRmg(product.getDetailRmg())
                .productImg(product.getProductImg())
                .productRmg(product.getProductRmg())
                .productDetail(product.getProductDetail())
                .build();
        return productRepository.save(entity);
    }
    public List<ProductDto> getShopAdminList(){
        List<Product> list = productRepository.findAll();
        List<ProductDto> plist = new ArrayList<>();
        for(Product p : list){
            plist.add(ProductDto.builder()
                    .productCode(p.getProductCode())
                    .productName(p.getProductName())
                    .category1(p.getCategory1())
                    .productCount(p.getProductCount())
                    .productAmount(p.getProductAmount())
                    .productView(p.getProductView())
                    .productSum(p.getProductSum())
                    .detailImg(p.getDetailImg())
                    .detailRmg(p.getDetailRmg())
                    .productImg(p.getProductImg())
                    .productRmg(p.getProductRmg())
                    .productDetail(p.getProductDetail())
                    .build());
        }
        log.info(plist.toString());
        return plist;
    }
    @Transactional
    public void deleteShopAdminList(Long productCode){

        productRepository.deleteById(productCode);
    }
    public ProductDto selectone(Long productCode){
        Product entity = productRepository.findById(productCode).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        return ProductDto.builder()
                .productCode(entity.getProductCode())
                .productName(entity.getProductName())
                .category1(entity.getCategory1())
                .productCount(entity.getProductCount())
                .productAmount(entity.getProductAmount())
                .detailImg(entity.getDetailImg())
                .detailRmg(entity.getDetailRmg())
                .productImg(entity.getProductImg())
                .productRmg(entity.getProductRmg())
                .productDetail(entity.getProductDetail())
                .productView(entity.getProductView())
                .build();
    }
    @Transactional
    public void updateProduct(Product product){
Product entity = productRepository.findById(product.getProductCode()).orElseThrow(() -> new RuntimeException("게시물 없음"));
        entity.setProductName(product.getProductName());
        entity.setCategory1(product.getCategory1());
        entity.setProductCount(product.getProductCount());
        entity.setProductAmount(product.getProductAmount());
        entity.setDetailImg(product.getDetailImg());
        entity.setDetailRmg(product.getDetailRmg());
        entity.setProductImg(product.getProductImg());
        entity.setProductRmg(product.getProductRmg());
        entity.setProductDetail(product.getProductDetail());
        productRepository.save(entity);
    }

    @Transactional
    public void updateProductCount(ProductDto productDto){
        Product entity = productRepository.findById(productDto.getProductCode()).orElseThrow(() -> new RuntimeException("게시물 없음"));
        entity.setProductView(productDto.getProductView());
        productRepository.save(entity);
    }

    @Transactional
    public void insertPayment(Bill bill){
        Bill entity = Bill.builder()
                .billNo(bill.getBillNo())
                .billDate(bill.getBillDate())
                .billName(bill.getBillName())
                .billPhone(bill.getBillPhone())
                .billEmail(bill.getBillEmail())
                .billAddress(bill.getBillAddress())
                .billAmount(bill.getBillAmount())
                .billAddressNo(bill.getBillAddressNo())
                .memberCode(bill.getMemberCode())
                .recipient(bill.getRecipient())
                .payName(bill.getPayName())
                .billRequest(bill.getBillRequest())
                .build();
        billRepository.save(entity);
    }

    @Transactional
    public void deletePayment(String billNo){
        Bill entity = billRepository.findById(billNo).orElseThrow(() -> new RuntimeException("없음"));
        billRepository.delete(entity);
    }

    public BillDto findBill(String billNo){
        Bill entity = billRepository.findById(billNo).orElseThrow(() -> new RuntimeException("없음"));
        return BillDto.builder()
                .billNo(entity.getBillNo())
                .billAmount(entity.getBillAmount())
                .memberCode(entity.getMemberCode())
                .payName(entity.getPayName())
                .build();
    }

    @Transactional
    public void successPayment(Bill bill){
        Bill entity = billRepository.findById(bill.getBillNo()).orElseThrow(() -> new RuntimeException("없음"));
        entity.setBillKind(bill.getBillKind());
        entity.setBillNow("결제완료");
        entity.setPaymentkey(bill.getPaymentkey());
        billRepository.save(entity);
    }

    @Transactional
    public void updateProductCount(Long productCode, Long productCount, Long productSum){
        Product entity = productRepository.findById(productCode).orElseThrow(() -> new RuntimeException("게시물 없음"));
        entity.setProductCount(productCount);
        entity.setProductSum(productSum);
        productRepository.save(entity);
    }

    @Transactional
    public void insertBillDetail(BillDetailDto bill){
        BillDetail entity = BillDetail.builder()
                .billNo(bill.getBillNo())
                .memberCode(bill.getMemberCode())
                .productAmount(bill.getProductAmount())
                .productName(bill.getProductName())
                .billCount(bill.getBillCount())
                .productCode(bill.getProductCode())
                .build();
        billDetailRepository.save(entity);
    }

    @Transactional
    public void deleteBillDetail(String billNo){
        List<BillDetail> entity = billDetailRepository.findByBillNo(billNo);
        for(BillDetail p : entity){
            billDetailRepository.deleteById(p.getBD_NO());
        }
    }


    public CartDto findCart(Cart cart) {
        Cart entity = cartRepository.findCartProduct(cart.getMemberCode(), cart.getProductCode());
        if(entity == null){
            return null;
        }else {
            return CartDto.builder()
                    .productCode(entity.getProductCode())
                    .productName(entity.getProductName())
                    .productRmg(entity.getProductRmg())
                    .billCount(entity.getBillCount())
                    .productAmount(entity.getProductAmount())
                    .memberCode(entity.getMemberCode())
                    .build();
        }
    }

    @Transactional
    public void addCart(Cart cart){
        Cart entity = Cart.builder()
                    .cartCode(cart.getCartCode())
                    .productCode(cart.getProductCode())
                    .productName(cart.getProductName())
                    .productRmg(cart.getProductRmg())
                    .billCount(cart.getBillCount())
                    .productAmount(cart.getProductAmount())
                    .memberCode(cart.getMemberCode())
                    .productCount(cart.getProductCount())
                    .build();

        cartRepository.save(entity);
    }

    public List<CartDto> getCartList(String memberCode){
        List<Cart> list = cartRepository.findCartList(memberCode);
        List<CartDto> plist = new ArrayList<>();
        for(Cart p : list){
            plist.add(CartDto.builder()
                    .productCode(p.getProductCode())
                    .productName(p.getProductName())
                    .productRmg(p.getProductRmg())
                    .billCount(p.getBillCount())
                    .cartCode(p.getCartCode())
                    .productCount(p.getProductCount())
                    .productAmount(p.getProductAmount())
                    .memberCode(p.getMemberCode())
                    .build());
        }
        log.info(plist.toString());
        return plist;
    }

    @Transactional
    public void deleteCartList(String cartCode){
        cartRepository.deleteById(cartCode);
    }

    public List<BillDto> getPaymentResult(){
        List<Bill> list = billRepository.findAll();
        List<BillDto> plist = new ArrayList<>();
        for(Bill p : list){
            plist.add(BillDto.builder()
                    .billNo(p.getBillNo())
                    .billDate(p.getBillDate())
                    .billName(p.getBillName())
                    .billPhone(p.getBillPhone())
                    .billEmail(p.getBillEmail())
                    .billAddress(p.getBillAddress())
                    .billAmount(p.getBillAmount())
                    .billAddressNo(p.getBillAddressNo())
                    .memberCode(p.getMemberCode())
                    .recipient(p.getRecipient())
                    .payName(p.getPayName())
                    .billRequest(p.getBillRequest())
                    .billKind(p.getBillKind())
                    .billNow(p.getBillNow())
                    .invNo(p.getInvNo())
                    .billCancle(p.getBillCancle())
                    .paymentkey(p.getPaymentkey())
                    .build());
        }
        log.info(plist.toString());
        return plist;

    }

    public List<BillDetailDto> getPaymentResultDetail(String billNo){
        List<BillDetail> entity = billDetailRepository.findByBillNo(billNo);
        List<BillDetailDto> plist = new ArrayList<>();
        for(BillDetail p : entity){
            plist.add(BillDetailDto.builder()
                    .billNo(p.getBillNo())
                    .memberCode(p.getMemberCode())
                    .productAmount(p.getProductAmount())
                    .productName(p.getProductName())
                    .billCount(p.getBillCount())
                    .productCode(p.getProductCode())
                    .build());
        }
        log.info(plist.toString());
        return plist;

    }

    @Transactional
    public void updateInvNo(String billNo, String invNo){
        Bill bill = billRepository.findById(billNo).orElseThrow(() -> new RuntimeException("없음"));
        bill.setInvNo(invNo);
        bill.setBillNow("배송중");
        billRepository.save(bill);
    }
    public List<BillDto> getPaymentUser(String memberCode){
        List<Bill> list = billRepository.findByMemberCode(memberCode);
        List<BillDto> plist = new ArrayList<>();
        for(Bill p : list){
            plist.add(BillDto.builder()
                    .billNo(p.getBillNo())
                    .billDate(p.getBillDate())
                    .billName(p.getBillName())
                    .billPhone(p.getBillPhone())
                    .billEmail(p.getBillEmail())
                    .billAddress(p.getBillAddress())
                    .billAmount(p.getBillAmount())
                    .billAddressNo(p.getBillAddressNo())
                    .memberCode(p.getMemberCode())
                    .recipient(p.getRecipient())
                    .payName(p.getPayName())
                    .billRequest(p.getBillRequest())
                    .billKind(p.getBillKind())
                    .billNow(p.getBillNow())
                    .invNo(p.getInvNo())
                    .billCancle(p.getBillCancle())
                    .build());
        }
        log.info(plist.toString());
        return plist;

    }

    @Transactional
    public void updateCancel(String billNo, String billCancle){
        Bill bill = billRepository.findById(billNo).orElseThrow(() -> new RuntimeException("없음"));
        bill.setBillCancle(billCancle);
        billRepository.save(bill);
    }

    @Transactional
    public void paymentCancel(String billNo){
        Bill bill = billRepository.findById(billNo).orElseThrow(() -> new RuntimeException("없음"));
        bill.setBillNow("취소완료");
        billRepository.save(bill);
    }

    @Transactional
    public void updateDeliOk(String billNo){
        Bill bill = billRepository.findById(billNo).orElseThrow(() -> new RuntimeException("없음"));
        bill.setBillNow("배송완료");
        billRepository.save(bill);
    }







}
