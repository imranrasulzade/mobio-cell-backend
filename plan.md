# Next-Chat Context Plan (Mobio Cell Backend)

Bu fayl bu chatda edilən böyük dəyişikliklərin sıx xülasəsidir.
Növbəti chatda sadəcə bu faylı oxuyub davam et.

## 0) Prioritet məqsəd
Monorepo daxilində end-to-end axın:
- user/number/package/balance/billing/notification inteqrasiyası
- event + queue + DLQ/retry
- default package fallback
- minute-rate ilə balansdan çıxılma
- balance history + billing transaction + notification write
- `@PreAuthorize` + DB-driven RBAC (`roles`, `api_endpoints`, `role_api_permissions`)
- Redis cache (aktiv tarif)

---

## 1) Əvvəlki tapşırıqdan tətbiq edilən əsas düzəlişlər (1,2,3,4,5,8,9,10,11)

### 1.1 Distributed consistency (auth sign-up compensation)
- `ms-auth` sign-up axınında remote fail olarsa rollback compensation əlavə edilib.
- Profil və nömrə üçün delete-by-user endpointləri əlavə olunub və auth-dan çağırılır.

Əsas fayllar:
- `ms-auth/src/main/java/com/example/msauth/service/impl/AuthServiceImpl.java`
- `ms-auth/src/main/java/com/example/msauth/client/UserProfileClient.java`
- `ms-auth/src/main/java/com/example/msauth/client/NumberClient.java`
- `ms-user/src/main/java/com/example/msuser/controller/UserProfileController.java`
- `ms-number/src/main/java/com/example/msnumber/controller/PhoneNumberController.java`

### 1.2 Outbox (`ms-number`)
- Phone number save + event enqueue transaction daxilindədir.
- Rabbit publish birbaşa service-dən çıxarılıb, `OutboxService` scheduler dispatch edir.

Əsas fayllar:
- `ms-number/src/main/java/com/example/msnumber/entity/OutboxEvent.java`
- `ms-number/src/main/java/com/example/msnumber/repositories/OutboxEventRepository.java`
- `ms-number/src/main/java/com/example/msnumber/queue/OutboxService.java`
- `ms-number/src/main/resources/db/changelog/changes/003-create-outbox-events.yaml`

### 1.3 Idempotency
- balance və number-package default assign üçün duplicate event guard əlavə edilib.
- DB unique constraintlər əlavə edilib.

Fayllar:
- `ms-balance/src/main/java/com/example/msbalance/service/BalanceService.java`
- `ms-package/src/main/java/com/example/mspackage/service/impl/NumbersPackageServiceImpl.java`
- `ms-balance/src/main/resources/db/changelog/changes/002-add-unique-balance-phone-number.yaml`
- `ms-package/src/main/resources/db/changelog/changes/002-add-unique-active-number-package.yaml`

### 1.4 Validation
- DTO-lara bean validation + controller-lərə `@Valid` əlavə olunub.

Nümunə:
- `ms-auth/.../request/SignUpRequest.java`, `SignInRequest.java`
- `ms-number/.../request/PhoneNumberRequest.java`
- `ms-package/.../request/PackageRequest.java`
- `ms-user/.../dto/UserProfileDto.java`

### 1.5 RBAC authority boşluğu
- `ms-auth` token claim-lərinə role/user_id əlavə edilib.
- `User#getAuthorities` artıq `ROLE_<role>` qaytarır.

Fayllar:
- `ms-auth/src/main/java/com/example/msauth/entity/User.java`
- `ms-auth/src/main/java/com/example/msauth/service/impl/JwtService.java`
- `ms-auth/src/main/resources/db/changelog/changes/004-add-role-to-users.yaml`

### 1.6 `updateUserProfile` implement
- `ms-user` update metodu və endpoint implement edildi.

### 1.7 `deletePackage` not-found fix
- əvvəlcə find edilir, yoxdursa NotFound qaytarır.

---

## 2) Bu chatda edilən böyük funksional genişlənmə

## 2.1 Package service (`ms-package`)

### 2.1.1 Default package id=1-dən çıxarıldı
- Artıq default seçim `is_default=1` üzərindədir.
- Default tapılmasa runtime fallback package yaradılır (`Default Starter`).

Fayllar:
- `ms-package/src/main/java/com/example/mspackage/service/impl/NumbersPackageServiceImpl.java`
- `ms-package/src/main/java/com/example/mspackage/repositories/PackageRepository.java`

### 2.1.2 Yeni sütunlar və seed
- `packages`: `minute_rate`, `is_default`
- 2 seed package insert:
  - `Default Starter` (default)
  - `Standard Talk`

Migration:
- `ms-package/src/main/resources/db/changelog/changes/003-add-default-and-minute-rate-to-packages.yaml`

### 2.1.3 Aktiv tarif API
- `GET /api/package/active/by-number/{numberId}`

Fayllar:
- `ms-package/src/main/java/com/example/mspackage/controller/PackageController.java`
- `ms-package/src/main/java/com/example/mspackage/service/NumbersPackageService.java`
- `ms-package/src/main/java/com/example/mspackage/response/ActiveTariffResponse.java`

### 2.1.4 Retry + DLQ (default-package listener)
- `@RabbitListener` üçün 5 attempt retry interceptor + reject/no-requeue (DLQ) əlavə edilib.

Fayl:
- `ms-package/src/main/java/com/example/mspackage/configs/RabbitConfig.java`

---

## 2.2 Balance service (`ms-balance`)

### 2.2.1 Yeni API-lər
- `GET /api/balances/{numberId}`
- `GET /api/balances/{numberId}/history`
- `POST /api/balances/{numberId}/topup`
- `POST /api/balances/{numberId}/consume-minutes`
- `DELETE /api/balances/{numberId}`

Fayl:
- `ms-balance/src/main/java/com/example/msbalance/controller/BalanceController.java`

### 2.2.2 Business logic
- Topup və consume-minutes balansı dəyişir.
- Hər dəyişiklik `balance_history`-yə yazılır.
- Consume-minutes üçün aktiv tarif `ms-package`-dən feign ilə alınır.
- User rolunda number ownership check edilir (`ms-number` çağırışı ilə).

Fayl:
- `ms-balance/src/main/java/com/example/msbalance/service/BalanceService.java`

### 2.2.3 Redis cache
- Aktiv tarif cache (`activeTariff`) əlavə edilib.

Fayllar:
- `ms-balance/src/main/java/com/example/msbalance/service/TariffService.java`
- `ms-balance/src/main/resources/application-local.yaml`
- `ms-balance/src/main/resources/application-prod.yaml`

### 2.2.4 Event publish
- Balance dəyişəndə 2 exchange-ə publish:
  - billing
  - notification

Fayllar:
- `ms-balance/src/main/java/com/example/msbalance/queue/EventPublisher.java`
- `ms-balance/src/main/java/com/example/msbalance/configs/RabbitTopologyProps.java`
- `ms-balance/src/main/java/com/example/msbalance/configs/RabbitConfig.java`

QEYD: payload class-loader problemi olmaması üçün event payload `Map<String,Object>` kimi publish edilir.

### 2.2.5 RBAC (DB-driven)
- `@PreAuthorize("@rbacService.hasAccess(...)")` tətbiq olunub.
- DB-dən role-endpoint check var.

Fayllar:
- `ms-balance/src/main/java/com/example/msbalance/security/RbacService.java`
- `ms-balance/src/main/java/com/example/msbalance/repositories/RoleApiPermissionRepository.java`
- `ms-balance/src/main/resources/db/changelog/changes/003-seed-rbac-for-balance-apis.yaml`

---

## 2.3 Billing service (`ms-billing`)

### 2.3.1 Queue listener + transaction save
- `balance.changed` event consume edilir.
- `transactions` cədvəlinə save edilir.

Fayllar:
- `ms-billing/src/main/java/com/example/msbilling/queue/BalanceEventListener.java`
- `ms-billing/src/main/java/com/example/msbilling/service/impl/TransactionServiceImpl.java`
- `ms-billing/src/main/java/com/example/msbilling/repositories/TransactionRepository.java`
- `ms-billing/src/main/java/com/example/msbilling/repositories/TransactionSourceRepository.java`
- `ms-billing/src/main/java/com/example/msbilling/repositories/TransactionTypeRepository.java`

### 2.3.2 Rabbit config/topology
- balance billing queue/dlq config əlavə edilib.

Fayllar:
- `ms-billing/src/main/java/com/example/msbilling/configs/RabbitConfig.java`
- `ms-billing/src/main/java/com/example/msbilling/configs/RabbitTopologyProps.java`

### 2.3.3 Seed dictionary
- `trans_sources`, `trans_types` seed edildi.

Migration:
- `ms-billing/src/main/resources/db/changelog/changes/002-seed-transaction-dictionaries.yaml`

### 2.3.4 Billing API
- `GET /api/transactions/by-number/{numberId}`

Fayl:
- `ms-billing/src/main/java/com/example/msbilling/controller/TransactionController.java`

---

## 2.4 Notification service (`ms-notification`)

### 2.4.1 Queue listener + DB write
- `balance.changed` event consume edilir.
- `notifications` cədvəlinə message save edilir, log atılır.

Fayllar:
- `ms-notification/src/main/java/com/example/msnotification/queue/BalanceNotificationListener.java`
- `ms-notification/src/main/java/com/example/msnotification/repositories/NotificationRepository.java`

### 2.4.2 Rabbit config/topology
- balance notification queue/dlq config əlavə edilib.

Fayllar:
- `ms-notification/src/main/java/com/example/msnotification/configs/RabbitConfig.java`
- `ms-notification/src/main/java/com/example/msnotification/configs/RabbitTopologyProps.java`

### 2.4.3 Notification API
- `GET /api/notifications/by-number/{numberId}`

Fayl:
- `ms-notification/src/main/java/com/example/msnotification/controller/NotificationController.java`

---

## 2.5 Number service (`ms-number`) əlavə imkanlar

- `GET /api/phone-numbers/{numberId}` əlavə edildi (balance ownership check üçün)
- `GET /api/phone-numbers/by-user/{userId}`
- `POST /api/phone-numbers/me` (user özü üçün number əlavə edə bilir)

Fayl:
- `ms-number/src/main/java/com/example/msnumber/controller/PhoneNumberController.java`

RBAC seed və DB-driven preauthorize:
- `ms-number/src/main/java/com/example/msnumber/security/RbacService.java`
- `ms-number/src/main/resources/db/changelog/changes/004-seed-rbac-for-number-apis.yaml`

---

## 2.6 Gateway / Auth integ

- Gateway JWT filter user məlumatını downstream-a header olaraq verir:
  - `X-User-Id`
  - `X-User-Role`

Fayl:
- `ms-gateway/src/main/java/com/example/msgateway/filter/JwtValidationGlobalFilter.java`

- Yeni gateway route-lar:
  - `/api/billing-service/** -> 8086`
  - `/api/notification-service/** -> 8087`

Fayl:
- `ms-gateway/src/main/java/com/example/msgateway/configs/GatewayRoutesConfig.java`

---

## 3) Internal-key security
Aşağıdakı servislərə internal key filter/security əlavə olunub:
- `ms-user`
- `ms-number`
- `ms-package`
- `ms-balance`
- `ms-billing`
- `ms-notification`

Filterlər `X-Internal-Key` yoxlayır, həmçinin `X-User-Id`, `X-User-Role` əsasında authentication qurur.

---

## 4) Konfiq qeydləri

- `ms-billing` local/prod port: `8086`
- `ms-notification` local/prod port: `8087`
- `ms-number`, `ms-package`, `ms-balance`, `ms-billing`, `ms-notification` prod-da rabbit host `rabbitmq` verilib.
- `ms-balance` redis cache local `localhost:6379`, prod `redis:6379`.

---

## 5) Vacib open points (növbəti chatda ilk baxılacaq)

1. Compile/test tam run edilməyib (mühitdə `services.gradle.org` network blok idi).
2. `ms-number` exception message seed-lərində yeni `PHONE_NUMBER_NOT_FOUND` üçün message əlavə etmək lazımdır (i18n consistency).
3. `RbacService` hazırda path string exact-matching edir (seed path ilə eyni olmalıdır). İstəsən ant-path matcher və ya normalized matcher ilə gücləndirilə bilər.
4. Dynamic DB-driven authorization hazırda `ms-balance/ms-package/ms-number`-da var; istəsən eyni pattern `ms-billing/ms-notification` üçün də endpoint-level DB seed ilə tam uyğunlaşdırıla bilər.
5. Integration testlər lazımdır:
   - sign-up -> number outbox -> default package + initial balance
   - topup/consume -> billing transaction + notification row
   - user own-number authorization check

---

## 6) Növbəti chat üçün qısa prompt nümunəsi
"`/Users/imran/IdeaProjects/mobio-cell-backend/plan.md` oxu, open points 1-5-i sırayla həll et, əvvəl compile+test keçirt, sonra qalan düzəlişləri et."
