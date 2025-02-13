# transportation-management-app
### **🚀 Transportation Management Application (Spring Boot)**
This guide will walk you through **creating a Transportation Management Application** using **Spring Boot**. Below is the **project structure**, followed by the **Java classes**.

---

## **📁 Project Structure (Maven-based)**
```
transportation-management-app/
│── src/
│   ├── main/
│   │   ├── java/com/example/transportation/
│   │   │   ├── TransportationManagementApp.java
│   │   │   ├── controller/
│   │   │   │   ├── VehicleController.java
│   │   │   │   ├── DriverController.java
│   │   │   ├── model/
│   │   │   │   ├── Vehicle.java
│   │   │   │   ├── Driver.java
│   │   │   ├── repository/
│   │   │   │   ├── VehicleRepository.java
│   │   │   │   ├── DriverRepository.java
│   │   │   ├── service/
│   │   │   │   ├── VehicleService.java
│   │   │   │   ├── DriverService.java
│   ├── resources/
│   │   ├── application.properties
│── pom.xml
```
---

## **📌 1. Create `pom.xml`**
This **Maven configuration** file defines dependencies for **Spring Boot, JPA, PostgreSQL/MySQL, and Lombok**.

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example.transportation</groupId>
    <artifactId>transportation-management-app</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <name>Transportation Management Application</name>

    <properties>
        <java.version>17</java.version>
        <spring.boot.version>2.7.5</spring.boot.version>
    </properties>

    <dependencies>
        <!-- Spring Boot Starter Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>${spring.boot.version}</version>
        </dependency>

        <!-- Spring Boot Starter JPA -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
            <version>${spring.boot.version}</version>
        </dependency>

        <!-- Database Driver (H2 Embedded) -->
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>2.2.224</version> 
            <scope>runtime</scope>
       </dependency>

        <!-- Lombok for cleaner code -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.22</version>
            <scope>provided</scope>
        </dependency>

        <!-- Spring Boot Starter Test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <version>${spring.boot.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>javax.xml.bind</groupId>
            <artifactId>jaxb-api</artifactId>
            <version>2.3.1</version>
        </dependency>
        <dependency>
            <groupId>org.glassfish.jaxb</groupId>
            <artifactId>jaxb-runtime</artifactId>
            <version>2.3.1</version>
        </dependency>
     
    </dependencies>

    <build>
    <plugins>
        <!-- Maven Compiler Plugin: Set Java Source & Target Versions -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.8.1</version>
            <configuration>
                <source>${java.version}</source>
                <target>${java.version}</target>
            </configuration>
        </plugin>

        <!-- Spring Boot Maven Plugin -->
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <version>${spring.boot.version}</version>
        </plugin>
    </plugins>
</build>
</project>
```

---

## **📌 2. Create Main Class (`TransportationManagementApp.java`)**
```java
package com.example.transportation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TransportationManagementApp {
    public static void main(String[] args) {
        SpringApplication.run(TransportationManagementApp.class, args);
    }
}
```

---

## **📌 3. Create Model Classes**
### **Vehicle Model (`Vehicle.java`)**
```java
package com.example.transportation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String licensePlate;
    private String model;
    private String manufacturer;
    private int year;
}
```

### **Driver Model (`Driver.java`)**
```java
package com.example.transportation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "drivers")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String licenseNumber;
}
```

---

## **📌 4. Create Repository Interfaces**
### **Vehicle Repository (`VehicleRepository.java`)**
```java
package com.example.transportation.repository;

import com.example.transportation.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
```

### **Driver Repository (`DriverRepository.java`)**
```java
package com.example.transportation.repository;

import com.example.transportation.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
}
```

---

## **📌 5. Create Service Classes**
### **Vehicle Service (`VehicleService.java`)**
```java
package com.example.transportation.service;

import com.example.transportation.model.Vehicle;
import com.example.transportation.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }
}
```

### **Driver Service (`DriverService.java`)**
```java
package com.example.transportation.service;

import com.example.transportation.model.Driver;
import com.example.transportation.repository.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {
    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    public Driver saveDriver(Driver driver) {
        return driverRepository.save(driver);
    }
}
```

---

## **📌 6. Create REST Controllers**
### **Vehicle Controller (`VehicleController.java`)**
```java
package com.example.transportation.controller;

import com.example.transportation.model.Vehicle;
import com.example.transportation.service.VehicleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @PostMapping
    public Vehicle addVehicle(@RequestBody Vehicle vehicle) {
        return vehicleService.saveVehicle(vehicle);
    }
}
```

### **Driver Controller (`DriverController.java`)**
```java
package com.example.transportation.controller;

import com.example.transportation.model.Driver;
import com.example.transportation.service.DriverService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drivers")
public class DriverController {
    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping
    public List<Driver> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    @PostMapping
    public Driver addDriver(@RequestBody Driver driver) {
        return driverService.saveDriver(driver);
    }
}
```

---

## **📌 7. Configure `application.properties`**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/transport_db
spring.datasource.username=postgres
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```
If you are not using an external database, and instead want an embedded database like H2, add:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
```

---

### **🚀 Running the Application**
```bash
sudo mvn clean package
sudo mvn spring-boot:run
```
✔ **Access REST endpoints via:**  
- `GET http://localhost:8080/vehicles`
- `POST http://localhost:8080/vehicles`
- `GET http://localhost:8080/drivers`
- `POST http://localhost:8080/drivers`

Would you like additional features? 🚀
