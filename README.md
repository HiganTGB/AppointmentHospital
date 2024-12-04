# Appointment Hospital
## Installation
1. Open Docker
   1. docker-compose -f docker-compose-one-database.yml up -d 
   2. docker-compose -f docker-compose.yaml up -d
2. Start eureka Server
3. Start all service 
4. Start gateway
5. Start Client

## Api doc (Swagger-UI)
http://localhost:9999/swagger-ui.html


## Quy trình đặt lịch khám bệnh

1. Chọn hồ sơ ( lấy patientProfile)
2. Chọn chuyên khoa (Lấy danh sách specialty)
3. Chọn ngày khám (Lấy danh sách available date)
4. Chọn giờ khám ( lấy Schedule , vòng lặp call api để lấy giá trị )

   
