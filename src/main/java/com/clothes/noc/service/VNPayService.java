package com.clothes.noc.service;

import com.clothes.noc.config.VNPayConfig;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VNPayService {
    private final VNPayConfig config;
    public String createOrder(HttpServletRequest request, int amount, String orderInfor, String serverOrigin) {
        //Các bạn có thể tham khảo tài liệu hướng dẫn và điều chỉnh các tham số
        String vnpVersion = "2.1.0";
        String vnpCommand = "pay";
        String vnpTxnRef = config.getRandomNumber(8);
        String vnpIpAddr = config.getIpAddress(request);
        String vnpTmnCode = config.getVnpTmnCode();
        String orderType = "order-type";

        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnpVersion", vnpVersion);
        vnpParams.put("vnpCommand", vnpCommand);
        vnpParams.put("vnpTmnCode", vnpTmnCode);
        vnpParams.put("vnpAmount", String.valueOf(amount * 100));
        vnpParams.put("vnpCurrCode", "VND");

        vnpParams.put("vnpTxnRef", vnpTxnRef);
        vnpParams.put("vnpOrderInfo", orderInfor);
        vnpParams.put("vnpOrderType", orderType);

        String locate = "vn";
        vnpParams.put("vnpLocale", locate);

        String returnUrl = serverOrigin.concat(config.getVnpReturnUrl());
        vnpParams.put("vnpReturnUrl", returnUrl);
        vnpParams.put("vnpIpAddr", vnpIpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = formatter.format(cld.getTime());
        vnpParams.put("vnpCreateDate", vnpCreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnpExpireDate = formatter.format(cld.getTime());
        vnpParams.put("vnpExpireDate", vnpExpireDate);

        ArrayList<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnpParams.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String salt = config.getVnpHashSecret();
        String vnpSecureHash = config.hmacSHA512(salt, hashData.toString());
        queryUrl += "&vnpSecureHash=" + vnpSecureHash;
        return config.getVnpPayUrl() + "?" + queryUrl;
    }

    public int orderReturn(HttpServletRequest request) {
        HashMap<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName;
            String fieldValue;
            fieldName = URLEncoder.encode(params.nextElement(), StandardCharsets.US_ASCII);
            fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnpSecureHash = request.getParameter("vnpSecureHash");
        fields.remove("vnpSecureHashType");
        fields.remove("vnpSecureHash");
        String signValue = config.hashAllFields(fields);
        if (signValue.equals(vnpSecureHash)) {
            if ("00".equals(request.getParameter("vnpTransactionStatus"))) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }
}
