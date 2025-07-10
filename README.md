# Order Service

Order Service, kullanıcılardan sipariş alma ve SAGA mimarisi üzerinden süreci başlatma görevini üstlenir. Sipariş oluşturulduğunda `payment-requested` event'i yayınlanır.

İlişkili repolar:

- [Payment Service](https://github.com/mabattal/saga-payment-service)
- [Inventory Service](https://github.com/mabattal/saga-inventory-service)

---

## 🧩 Özellikler

- Sipariş oluşturma
- Kafka ile diğer servisleri tetikleme
- Sipariş durum güncellemeleri (`CREATED`, `COMPLETED`, `IN_PROGRES`, `FAILED_PAYMENT`, `FAILED_INVENTORY`)

---

## ⚙️ Konfigürasyon

**application.yml**
```yaml
server:
  port: 8081

spring:
  application:
    name: order-service

  datasource:
    url: jdbc:postgresql://localhost:5432/sagaOrderDb
    username: postgres
    password: 1
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect

  kafka:
    address: ${KAFKA_HOST:localhost}:${KAFKA_PORT:9092}
    group-id: order-service-group
    topic:
      orderCreated: order-created
      paymentRequested: payment-requested
      paymentCompleted: payment-completed
      paymentFailed: payment-failed
      inventoryReserved: inventory-reserved
      inventoryFailed: inventory-failed
    consumer:
      enable-auto-commit: false
      auto-offset-reset: earliest
      properties:
        max.poll.records: 5
        spring.json.trusted-packages: '*'

    listener:
      ack-mode: manual
```


---

## 🚀 Kafka Event Akışı

- ➡️ payment-requested ➝ payment-service
- ⬅️ payment-completed veya payment-failed
- ➡️ order-created ➝ inventory-service
- ⬅️ inventory-reserved veya inventory-failed
- ➡️ refund-payment ➝ payment-service

## 🛠️ Kullanılan Teknolojiler

- Spring Boot
- Spring Kafka
- PostgreSQL
