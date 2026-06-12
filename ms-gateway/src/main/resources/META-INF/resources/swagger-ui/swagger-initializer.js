window.onload = function () {
  window.ui = SwaggerUIBundle({
    urls: [
      { name: "auth-service", url: "/v3/api-docs/auth-service" },
      { name: "user-service", url: "/v3/api-docs/user-service" },
      { name: "number-service", url: "/v3/api-docs/number-service" },
      { name: "balance-service", url: "/v3/api-docs/balance-service" },
      { name: "package-service", url: "/v3/api-docs/package-service" },
      { name: "billing-service", url: "/v3/api-docs/billing-service" },
      { name: "notification-service", url: "/v3/api-docs/notification-service" }
    ],
    "urls.primaryName": "auth-service",
    dom_id: "#swagger-ui",
    deepLinking: true,
    displayRequestDuration: true,
    persistAuthorization: true,
    presets: [
      SwaggerUIBundle.presets.apis,
      SwaggerUIStandalonePreset
    ],
    plugins: [
      SwaggerUIBundle.plugins.DownloadUrl
    ],
    layout: "StandaloneLayout",
    requestInterceptor: function (req) {
      var jwt = localStorage.getItem("swagger_jwt_token");
      var acceptLang = localStorage.getItem("swagger_accept_language") || "az";
      var internalKey = localStorage.getItem("swagger_internal_key");
      var userRole = localStorage.getItem("swagger_user_role");
      var userId = localStorage.getItem("swagger_user_id");
      var serviceByPort = {
        "8081": "auth-service",
        "8082": "user-service",
        "8083": "number-service",
        "8084": "balance-service",
        "8085": "package-service",
        "8086": "billing-service",
        "8087": "notification-service"
      };

      req.headers["Accept-Language"] = acceptLang;

      // Force "Try it out" calls to pass through gateway to avoid browser CORS on direct service URLs.
      try {
        var parsed = new URL(req.url, window.location.origin);
        var isAbsoluteServiceUrl = parsed.host && parsed.host !== window.location.host;
        var targetService = serviceByPort[parsed.port];
        if (isAbsoluteServiceUrl && targetService) {
          var normalizedPath = parsed.pathname.replace(/^\/+/, "");
          if (normalizedPath.indexOf("api/") === 0) {
            normalizedPath = normalizedPath.substring(4);
          }
          req.url =
            window.location.origin +
            "/api/" +
            targetService +
            "/" +
            normalizedPath +
            (parsed.search || "");
        }
      } catch (e) {
        // Keep original URL if parsing fails.
      }

      if (jwt && !req.headers["Authorization"]) {
        req.headers["Authorization"] = "Bearer " + jwt;
      }
      if (internalKey) {
        req.headers["X-Internal-Key"] = internalKey;
      }
      if (userRole) {
        req.headers["X-User-Role"] = userRole;
      }
      if (userId) {
        req.headers["X-User-Id"] = userId;
      }

      return req;
    }
  });
};
