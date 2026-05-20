package com.example.demo;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    /**
     * Salesforce Apex callout:
     * POST /auth/bootstrap
     */
    @PostMapping(
            value = "/bootstrap",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String, Object> bootstrap(
            @RequestHeader(value = "Authorization", required = false)
            String authHeader
    ) {

        String jwtToken = "";

        if (authHeader != null
                && authHeader.startsWith("Bearer ")) {

            jwtToken = authHeader.substring(7);
        }

        // 返回给 Salesforce iframe URL
        String iframeUrl =
                "https://jwt-px9l.onrender.com/auth/view?token="
                        + URLEncoder.encode(
                        jwtToken,
                        StandardCharsets.UTF_8
                );

        Map<String, Object> result = new HashMap<>();

        result.put("iframeUrl", iframeUrl);

        return result;
    }

    /**
     * Health Check
     */
    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    /**
     * LWC iframe 打开的页面
     */
    @GetMapping(
            value = "/view",
            produces = MediaType.TEXT_HTML_VALUE
    )
    public String view(
            @RequestParam(required = false)
            String token
    ) {

        String payloadJson = "No JWT Found";

        String recordId = "";
        String email = "";

        try {

            if (token != null) {

                String[] parts = token.split("\\.");

                if (parts.length >= 2) {

                    String payload = parts[1];

                    // Base64URL padding
                    int padding =
                            4 - payload.length() % 4;

                    if (padding < 4) {
                        payload += "=".repeat(padding);
                    }

                    byte[] decoded =
                            Base64.getUrlDecoder()
                                    .decode(payload);

                    payloadJson =
                            new String(
                                    decoded,
                                    StandardCharsets.UTF_8
                            );

                    // 简单字符串提取
                    recordId =
                            extractValue(
                                    payloadJson,
                                    "recordId"
                            );

                    email =
                            extractValue(
                                    payloadJson,
                                    "email"
                            );
                }
            }

        } catch (Exception e) {

            payloadJson =
                    "JWT Parse Error: "
                            + e.getMessage();
        }

        return """
                <html>
                <head>
                    <title>Java Page</title>

                    <style>

                        body{
                            font-family:Arial;
                            background:#f5f5f5;
                            padding:40px;
                        }

                        .card{
                            background:white;
                            border-radius:12px;
                            padding:30px;
                            box-shadow:
                              0 2px 12px rgba(0,0,0,0.15);
                        }

                        .item{
                            margin-bottom:20px;
                        }

                        .label{
                            font-weight:bold;
                            color:#666;
                        }

                        pre{
                            background:#111;
                            color:#00ff99;
                            padding:20px;
                            overflow:auto;
                        }

                    </style>

                </head>

                <body>

                    <div class='card'>

                        <h1>
                            Java Page Loaded Successfully
                        </h1>

                        <div class='item'>
                            <div class='label'>
                                recordId
                            </div>
                            <div>
                                """
                + recordId +
                """
                            </div>
                        </div>

                        <div class='item'>
                            <div class='label'>
                                email
                            </div>
                            <div>
                                """
                + email +
                """
                            </div>
                        </div>

                        <h3>
                            Full JWT Payload
                        </h3>

                        <pre>
                """
                + payloadJson +
                """
                        </pre>

                    </div>

                </body>
                </html>
                """;
    }

    /**
     * 简单 JSON string 提取
     */
    private String extractValue(
            String json,
            String key
    ) {

        try {

            String target =
                    "\"" + key + "\":\"";

            int start =
                    json.indexOf(target);

            if (start < 0) {
                return "";
            }

            start += target.length();

            int end =
                    json.indexOf(
                            "\"",
                            start
                    );

            return json.substring(
                    start,
                    end
            );

        } catch (Exception e) {
            return "";
        }
    }
}
