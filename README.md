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

---
Here is a **starter React Native application** that integrates with your **Spring Boot backend** for a **Transportation Management App**. This setup will allow your mobile app to interact with the backend using REST APIs.

---

### **1. Prerequisites**
Ensure you have the following installed:
- **Node.js** (>= 14.x)
- **React Native CLI** (`npm install -g react-native-cli`)
- **Android Studio** (for Android) or **Xcode** (for iOS)
- **Spring Boot Backend** (running on `http://localhost:8080` or a deployed URL)

---

### **2. Create a New React Native Project**
Run the following command in your terminal:

```sh
npx react-native init TransportationApp
cd TransportationApp
npm install axios react-navigation react-native-gesture-handler
```

---

### **3. Project Structure**
```
TransportationApp/
│── src/
│   ├── screens/
│   │   ├── HomeScreen.js
│   │   ├── VehiclesScreen.js
│   ├── components/
│   │   ├── VehicleCard.js
│   ├── services/
│   │   ├── api.js
│── App.js
│── package.json
│── index.js
```

---

### **4. API Service (Connect to Spring Boot)**
Create a file `src/services/api.js` to interact with the backend.

```javascript
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api'; // Change to your backend URL

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: { 'Content-Type': 'application/json' }
});

export const getVehicles = async () => {
  try {
    const response = await api.get('/vehicles');
    return response.data;
  } catch (error) {
    console.error('Error fetching vehicles:', error);
    return [];
  }
};

export const addVehicle = async (vehicleData) => {
  try {
    const response = await api.post('/vehicles', vehicleData);
    return response.data;
  } catch (error) {
    console.error('Error adding vehicle:', error);
    return null;
  }
};
```

---

### **5. Home Screen (Navigation)**
Create a new file `src/screens/HomeScreen.js`.

```javascript
import React from 'react';
import { View, Text, Button, StyleSheet } from 'react-native';

const HomeScreen = ({ navigation }) => {
  return (
    <View style={styles.container}>
      <Text style={styles.title}>Transportation Management</Text>
      <Button title="View Vehicles" onPress={() => navigation.navigate('Vehicles')} />
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: 'center', alignItems: 'center' },
  title: { fontSize: 24, fontWeight: 'bold', marginBottom: 20 }
});

export default HomeScreen;
```

---

### **6. Vehicles Screen (List of Vehicles)**
Create a new file `src/screens/VehiclesScreen.js`.

```javascript
import React, { useEffect, useState } from 'react';
import { View, FlatList, Text, ActivityIndicator, StyleSheet } from 'react-native';
import { getVehicles } from '../services/api';
import VehicleCard from '../components/VehicleCard';

const VehiclesScreen = () => {
  const [vehicles, setVehicles] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchVehicles();
  }, []);

  const fetchVehicles = async () => {
    const data = await getVehicles();
    setVehicles(data);
    setLoading(false);
  };

  return (
    <View style={styles.container}>
      {loading ? (
        <ActivityIndicator size="large" color="#0000ff" />
      ) : (
        <FlatList
          data={vehicles}
          keyExtractor={(item) => item.id.toString()}
          renderItem={({ item }) => <VehicleCard vehicle={item} />}
        />
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, padding: 10 }
});

export default VehiclesScreen;
```

---

### **7. Vehicle Card Component**
Create a new file `src/components/VehicleCard.js`.

```javascript
import React from 'react';
import { View, Text, StyleSheet } from 'react-native';

const VehicleCard = ({ vehicle }) => {
  return (
    <View style={styles.card}>
      <Text style={styles.name}>{vehicle.name}</Text>
      <Text>Type: {vehicle.type}</Text>
      <Text>Capacity: {vehicle.capacity}</Text>
    </View>
  );
};

const styles = StyleSheet.create({
  card: { padding: 15, marginBottom: 10, backgroundColor: '#f9f9f9', borderRadius: 5 },
  name: { fontSize: 18, fontWeight: 'bold' }
});

export default VehicleCard;
```

---

### **8. Configure Navigation**
Modify `App.js` to set up React Navigation.

```javascript
import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import HomeScreen from './src/screens/HomeScreen';
import VehiclesScreen from './src/screens/VehiclesScreen';

const Stack = createStackNavigator();

const App = () => {
  return (
    <NavigationContainer>
      <Stack.Navigator>
        <Stack.Screen name="Home" component={HomeScreen} />
        <Stack.Screen name="Vehicles" component={VehiclesScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
};

export default App;
```

---

### **9. Running the App**
- **For Android:**
  ```sh
  npx react-native run-android
  ```
- **For iOS (Mac users with Xcode installed):**
  ```sh
  npx react-native run-ios
  ```

---

### **10. Expected Behavior**
- The **Home screen** displays a title and a button to navigate to the vehicle list.
- The **Vehicles screen** fetches and displays vehicle data from your **Spring Boot backend**.
- **VehicleCard** components show vehicle details (name, type, capacity).

---

### **Next Steps**
- Add authentication (JWT) to secure API calls.
- Implement real-time tracking (WebSockets).
- Add forms for adding/updating vehicle records.

---
### **Adding Authentication (JWT) and WebSocket Integration to Your React Native Transportation App**
Since your backend is built with **Spring Boot**, we'll enhance your React Native app by adding:
1. **JWT Authentication** – Secure API requests.
2. **WebSocket Integration** – Enable real-time vehicle tracking.

---

## **1. Implementing JWT Authentication**
### **Backend (Spring Boot)**
Ensure your Spring Boot backend has JWT authentication enabled. If not, follow these steps:

#### **1.1. Add Dependencies to Spring Boot**
```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

#### **1.2. Configure JWT Authentication in Spring Boot**
Create a `JwtUtil.java` file to generate and validate tokens.

```java
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private String secretKey = "yourSecretKey";

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token, String username) {
        return (extractUsername(token).equals(username));
    }
}
```

#### **1.3. Secure API Endpoints**
Modify your Spring Boot API to require JWT authentication.

```java
@RestController
@RequestMapping("/api")
public class VehicleController {

    @GetMapping("/vehicles")
    public ResponseEntity<List<Vehicle>> getVehicles(@RequestHeader("Authorization") String token) {
        if (jwtUtil.validateToken(token.replace("Bearer ", ""), "user")) {
            return ResponseEntity.ok(vehicleService.getAllVehicles());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
```

---

### **2. Implement JWT Authentication in React Native**
#### **2.1. Install Secure Storage for JWT**
```sh
npm install @react-native-async-storage/async-storage axios
```

#### **2.2. Create Authentication Service (`src/services/auth.js`)**
```javascript
import AsyncStorage from '@react-native-async-storage/async-storage';
import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

export const login = async (username, password) => {
  try {
    const response = await axios.post(`${API_URL}/auth/login`, { username, password });
    if (response.data.token) {
      await AsyncStorage.setItem('token', response.data.token);
      return response.data;
    }
  } catch (error) {
    console.error('Login failed:', error);
  }
  return null;
};

export const getAuthToken = async () => {
  return await AsyncStorage.getItem('token');
};

export const logout = async () => {
  await AsyncStorage.removeItem('token');
};
```

#### **2.3. Modify API Service to Include JWT Token**
Update `src/services/api.js` to include JWT in requests.

```javascript
import axios from 'axios';
import { getAuthToken } from './auth';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({ baseURL: API_BASE_URL });

api.interceptors.request.use(async (config) => {
  const token = await getAuthToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, (error) => Promise.reject(error));

export const getVehicles = async () => {
  try {
    const response = await api.get('/vehicles');
    return response.data;
  } catch (error) {
    console.error('Error fetching vehicles:', error);
    return [];
  }
};
```

#### **2.4. Create Login Screen (`src/screens/LoginScreen.js`)**
```javascript
import React, { useState } from 'react';
import { View, Text, TextInput, Button, StyleSheet } from 'react-native';
import { login } from '../services/auth';

const LoginScreen = ({ navigation }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = async () => {
    const result = await login(username, password);
    if (result) {
      navigation.navigate('Home');
    } else {
      alert('Login failed');
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Login</Text>
      <TextInput style={styles.input} placeholder="Username" onChangeText={setUsername} />
      <TextInput style={styles.input} placeholder="Password" secureTextEntry onChangeText={setPassword} />
      <Button title="Login" onPress={handleLogin} />
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: 'center', padding: 20 },
  title: { fontSize: 24, fontWeight: 'bold', textAlign: 'center', marginBottom: 20 },
  input: { borderWidth: 1, padding: 10, marginBottom: 10 }
});

export default LoginScreen;
```

#### **2.5. Update Navigation in `App.js`**
```javascript
import { createStackNavigator } from '@react-navigation/stack';
import LoginScreen from './src/screens/LoginScreen';
import HomeScreen from './src/screens/HomeScreen';
import VehiclesScreen from './src/screens/VehiclesScreen';

const Stack = createStackNavigator();

const App = () => (
  <NavigationContainer>
    <Stack.Navigator initialRouteName="Login">
      <Stack.Screen name="Login" component={LoginScreen} />
      <Stack.Screen name="Home" component={HomeScreen} />
      <Stack.Screen name="Vehicles" component={VehiclesScreen} />
    </Stack.Navigator>
  </NavigationContainer>
);

export default App;
```

---

## **3. Adding WebSocket for Real-Time Vehicle Updates**
### **3.1. Enable WebSockets in Spring Boot**
Add this to your Spring Boot configuration.

```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
    }
}
```

### **3.2. Update React Native to Support WebSockets**
Install WebSocket dependencies:
```sh
npm install @stomp/stompjs
```

Create `src/services/websocket.js`:
```javascript
import { Client } from '@stomp/stompjs';

const socketURL = 'ws://localhost:8080/ws';

export const socketClient = new Client({
  brokerURL: socketURL,
  onConnect: () => console.log('Connected to WebSocket'),
  onDisconnect: () => console.log('Disconnected from WebSocket'),
});

export const subscribeToVehicleUpdates = (callback) => {
  socketClient.onStompError = (error) => console.error('WebSocket error:', error);
  socketClient.activate();
  
  socketClient.onConnect = () => {
    socketClient.subscribe('/topic/vehicles', (message) => {
      callback(JSON.parse(message.body));
    });
  };
};
```

Modify `VehiclesScreen.js` to subscribe to updates:
```javascript
import { subscribeToVehicleUpdates } from '../services/websocket';

useEffect(() => {
  subscribeToVehicleUpdates((updatedVehicle) => {
    setVehicles((prevVehicles) => prevVehicles.map(v => v.id === updatedVehicle.id ? updatedVehicle : v));
  });
}, []);
```

---

## **Conclusion**
- **JWT Authentication** secures API requests.
- **WebSockets** enable real-time vehicle tracking.

Would you like a UI update to show vehicle movement in real-time? 🚀
---
### **Adding Real-Time Vehicle Movement in the UI**
To visually display real-time vehicle movement, we'll integrate **React Native Maps** and update vehicle locations dynamically using **WebSockets**.

---

## **1. Install Required Packages**
```sh
npm install react-native-maps @react-native-maps/maps @stomp/stompjs
```
For **iOS**, install pods:
```sh
cd ios && pod install && cd ..
```

---

## **2. Update WebSocket Service to Handle Real-Time Vehicle Updates**
Modify `src/services/websocket.js` to handle vehicle location updates.

```javascript
import { Client } from '@stomp/stompjs';

const socketURL = 'ws://localhost:8080/ws';

export const socketClient = new Client({
  brokerURL: socketURL,
  debug: (str) => console.log(str), // Debugging WebSocket connection
  onConnect: () => console.log('Connected to WebSocket'),
  onDisconnect: () => console.log('Disconnected from WebSocket'),
});

export const subscribeToVehicleUpdates = (callback) => {
  socketClient.onStompError = (error) => console.error('WebSocket error:', error);
  socketClient.activate();
  
  socketClient.onConnect = () => {
    socketClient.subscribe('/topic/vehicles', (message) => {
      callback(JSON.parse(message.body));
    });
  };
};
```

---

## **3. Modify Vehicles Screen to Show Live Map Updates**
Modify `src/screens/VehiclesScreen.js` to display **vehicle locations** in real time.

```javascript
import React, { useEffect, useState } from 'react';
import { View, ActivityIndicator, StyleSheet } from 'react-native';
import MapView, { Marker } from 'react-native-maps';
import { getVehicles } from '../services/api';
import { subscribeToVehicleUpdates } from '../services/websocket';

const VehiclesScreen = () => {
  const [vehicles, setVehicles] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchVehicles();
    subscribeToVehicleUpdates((updatedVehicle) => {
      setVehicles((prevVehicles) =>
        prevVehicles.map((v) =>
          v.id === updatedVehicle.id ? updatedVehicle : v
        )
      );
    });
  }, []);

  const fetchVehicles = async () => {
    const data = await getVehicles();
    setVehicles(data);
    setLoading(false);
  };

  return (
    <View style={styles.container}>
      {loading ? (
        <ActivityIndicator size="large" color="#0000ff" />
      ) : (
        <MapView
          style={styles.map}
          initialRegion={{
            latitude: vehicles.length ? vehicles[0].latitude : 37.7749, // Default center
            longitude: vehicles.length ? vehicles[0].longitude : -122.4194,
            latitudeDelta: 0.1,
            longitudeDelta: 0.1,
          }}
        >
          {vehicles.map((vehicle) => (
            <Marker
              key={vehicle.id}
              coordinate={{
                latitude: vehicle.latitude,
                longitude: vehicle.longitude,
              }}
              title={vehicle.name}
              description={`Type: ${vehicle.type} | Capacity: ${vehicle.capacity}`}
            />
          ))}
        </MapView>
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1 },
  map: { flex: 1 },
});

export default VehiclesScreen;
```

---

## **4. Update Backend to Send Real-Time Location Updates**
Modify your **Spring Boot** controller to broadcast vehicle location changes.

```java
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class VehicleController {

    private final SimpMessagingTemplate messagingTemplate;
    private final VehicleService vehicleService;
    private final Random random = new Random();

    public VehicleController(SimpMessagingTemplate messagingTemplate, VehicleService vehicleService) {
        this.messagingTemplate = messagingTemplate;
        this.vehicleService = vehicleService;
    }

    @GetMapping("/vehicles")
    public List<Vehicle> getVehicles() {
        return vehicleService.getAllVehicles();
    }

    // Simulate real-time location updates every 5 seconds
    @Scheduled(fixedRate = 5000)
    public void sendVehicleUpdates() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        for (Vehicle vehicle : vehicles) {
            vehicle.setLatitude(vehicle.getLatitude() + (random.nextDouble() - 0.5) * 0.01);
            vehicle.setLongitude(vehicle.getLongitude() + (random.nextDouble() - 0.5) * 0.01);
            messagingTemplate.convertAndSend("/topic/vehicles", vehicle);
        }
    }
}
```

---

## **5. Testing the App**
1. **Run Spring Boot Backend**
   ```sh
   mvn spring-boot:run
   ```
2. **Run React Native App**
   ```sh
   npx react-native run-android
   ```
   OR
   ```sh
   npx react-native run-ios
   ```

---

## **6. Expected Behavior**
- The app **displays a map** with vehicle locations as markers.
- Vehicles **move dynamically** every **5 seconds**, simulating real-time tracking.
- React Native **receives WebSocket updates** and updates vehicle positions accordingly.

---

### **Next Steps**
- Add **real GPS tracking** using a GPS-enabled device.
- Implement a **backend database (PostgreSQL, MySQL, MongoDB)** for persistent tracking.
- Enhance UI with **real-time animations** for smoother movement.

Would you like me to implement real GPS tracking next? 🚀
---
### **Implementing Real GPS Tracking in the React Native Transportation App**
To track **real-time vehicle locations** using **GPS**, we will:
1. **Use React Native’s location services** to get GPS coordinates.
2. **Send GPS data to the Spring Boot backend**.
3. **Update the WebSocket service to broadcast real locations**.
4. **Visualize the movement on the map in real-time**.

---

## **1. Install Location Services in React Native**
First, install the required packages:
```sh
npm install @react-native-community/geolocation
npm install react-native-permissions
```
For **iOS**, update `Info.plist` (found in `ios/YourApp/Info.plist`) and add:
```xml
<key>NSLocationWhenInUseUsageDescription</key>
<string>We need access to your location for tracking</string>
<key>NSLocationAlwaysUsageDescription</key>
<string>We need access to track vehicle movement</string>
```
For **Android**, update `AndroidManifest.xml` (found in `android/app/src/main/AndroidManifest.xml`):
```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
```

---

## **2. Update React Native to Send GPS Data to Backend**
Modify `src/services/location.js` to send GPS coordinates to the backend.

```javascript
import Geolocation from '@react-native-community/geolocation';
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/vehicles/update-location';

export const trackVehicleLocation = (vehicleId) => {
  Geolocation.watchPosition(
    async (position) => {
      const { latitude, longitude } = position.coords;
      try {
        await axios.post(API_URL, { vehicleId, latitude, longitude });
      } catch (error) {
        console.error('Error sending location update:', error);
      }
    },
    (error) => console.error('Error getting location:', error),
    { enableHighAccuracy: true, distanceFilter: 10 }
  );
};
```

---

## **3. Call Location Tracking from the Vehicles Screen**
Modify `src/screens/VehiclesScreen.js` to start tracking.

```javascript
import { useEffect } from 'react';
import { trackVehicleLocation } from '../services/location';

useEffect(() => {
  const vehicleId = 1; // Replace with dynamic vehicle ID
  trackVehicleLocation(vehicleId);
}, []);
```

---

## **4. Update Spring Boot Backend to Handle GPS Updates**
Modify the `VehicleController.java` to accept GPS updates.

```java
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final SimpMessagingTemplate messagingTemplate;
    private final VehicleService vehicleService;

    public VehicleController(SimpMessagingTemplate messagingTemplate, VehicleService vehicleService) {
        this.messagingTemplate = messagingTemplate;
        this.vehicleService = vehicleService;
    }

    @PostMapping("/update-location")
    public void updateVehicleLocation(@RequestBody Vehicle vehicleUpdate) {
        Vehicle vehicle = vehicleService.updateLocation(vehicleUpdate.getId(), vehicleUpdate.getLatitude(), vehicleUpdate.getLongitude());
        messagingTemplate.convertAndSend("/topic/vehicles", vehicle);
    }
}
```

---

## **5. Implement Database Storage for GPS Updates**
Modify `VehicleService.java` to store GPS data in the database.

```java
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle updateLocation(Long vehicleId, double latitude, double longitude) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(vehicleId);
        if (optionalVehicle.isPresent()) {
            Vehicle vehicle = optionalVehicle.get();
            vehicle.setLatitude(latitude);
            vehicle.setLongitude(longitude);
            return vehicleRepository.save(vehicle);
        }
        return null;
    }
}
```

---

## **6. Run and Test**
1. Start the **Spring Boot backend**:
   ```sh
   mvn spring-boot:run
   ```
2. Start the **React Native app**:
   ```sh
   npx react-native run-android
   ```
   OR
   ```sh
   npx react-native run-ios
   ```

---

## **7. Expected Behavior**
- When the app runs, it requests **GPS permissions**.
- The **vehicle’s GPS location updates** every few seconds.
- The **backend receives and broadcasts** the updates via **WebSockets**.
- The **React Native app map updates in real-time**.

---

## **Next Steps**
- Add **GPS history tracking**.
- Implement **geofencing** (alerts if a vehicle leaves a defined area).
- Optimize **battery efficiency** with background tracking.

Would you like me to implement **GPS history tracking** next? 🚀
---
### **Implementing GPS History Tracking in the Transportation Management App**
To enable **GPS history tracking**, we will:
1. **Modify the database schema** to store location history.
2. **Update the backend service** to record each GPS update.
3. **Create a new API endpoint** to retrieve location history.
4. **Update the React Native app** to display a **vehicle's route history** on a map.

---

## **1. Modify Database Schema to Store GPS History**
Modify the `VehicleLocation` entity to store **timestamped location updates**.

### **1.1. Create a New Entity: `VehicleLocation`**
```java
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class VehicleLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    private double latitude;
    private double longitude;
    private LocalDateTime timestamp;

    public VehicleLocation() {}

    public VehicleLocation(Vehicle vehicle, double latitude, double longitude) {
        this.vehicle = vehicle;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
}
```

### **1.2. Create a Repository for Storing Locations**
```java
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VehicleLocationRepository extends JpaRepository<VehicleLocation, Long> {
    List<VehicleLocation> findByVehicleIdOrderByTimestampAsc(Long vehicleId);
}
```

---

## **2. Update Backend Service to Store GPS History**
Modify `VehicleService.java` to save location history.

```java
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleLocationRepository locationRepository;

    public VehicleService(VehicleRepository vehicleRepository, VehicleLocationRepository locationRepository) {
        this.vehicleRepository = vehicleRepository;
        this.locationRepository = locationRepository;
    }

    public Vehicle updateLocation(Long vehicleId, double latitude, double longitude) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(vehicleId);
        if (optionalVehicle.isPresent()) {
            Vehicle vehicle = optionalVehicle.get();
            vehicle.setLatitude(latitude);
            vehicle.setLongitude(longitude);
            vehicleRepository.save(vehicle);

            // Save GPS history
            VehicleLocation location = new VehicleLocation(vehicle, latitude, longitude);
            locationRepository.save(location);

            return vehicle;
        }
        return null;
    }

    public List<VehicleLocation> getVehicleLocationHistory(Long vehicleId) {
        return locationRepository.findByVehicleIdOrderByTimestampAsc(vehicleId);
    }
}
```

---

## **3. Create a New API Endpoint to Retrieve GPS History**
Modify `VehicleController.java` to expose a **GET API**.

```java
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/{vehicleId}/history")
    public List<VehicleLocation> getVehicleHistory(@PathVariable Long vehicleId) {
        return vehicleService.getVehicleLocationHistory(vehicleId);
    }
}
```

---

## **4. Update React Native to Display Vehicle Route on Map**
Modify `src/services/api.js` to fetch GPS history.

```javascript
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/vehicles';

export const getVehicleHistory = async (vehicleId) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${vehicleId}/history`);
    return response.data;
  } catch (error) {
    console.error('Error fetching location history:', error);
    return [];
  }
};
```

### **4.1. Create a New Screen to Show GPS Route**
Modify `src/screens/VehicleHistoryScreen.js`.

```javascript
import React, { useEffect, useState } from 'react';
import { View, ActivityIndicator, StyleSheet } from 'react-native';
import MapView, { Marker, Polyline } from 'react-native-maps';
import { getVehicleHistory } from '../services/api';

const VehicleHistoryScreen = ({ route }) => {
  const { vehicleId } = route.params;
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchVehicleHistory();
  }, []);

  const fetchVehicleHistory = async () => {
    const data = await getVehicleHistory(vehicleId);
    setHistory(data);
    setLoading(false);
  };

  return (
    <View style={styles.container}>
      {loading ? (
        <ActivityIndicator size="large" color="#0000ff" />
      ) : (
        <MapView
          style={styles.map}
          initialRegion={{
            latitude: history.length ? history[0].latitude : 37.7749,
            longitude: history.length ? history[0].longitude : -122.4194,
            latitudeDelta: 0.1,
            longitudeDelta: 0.1,
          }}
        >
          {history.map((loc, index) => (
            <Marker
              key={index}
              coordinate={{ latitude: loc.latitude, longitude: loc.longitude }}
              title={`Timestamp: ${loc.timestamp}`}
            />
          ))}
          <Polyline
            coordinates={history.map((loc) => ({
              latitude: loc.latitude,
              longitude: loc.longitude,
            }))}
            strokeWidth={3}
            strokeColor="blue"
          />
        </MapView>
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1 },
  map: { flex: 1 },
});

export default VehicleHistoryScreen;
```

---

## **5. Add Navigation to View GPS History**
Modify `App.js` to include the new screen.

```javascript
import { createStackNavigator } from '@react-navigation/stack';
import HomeScreen from './src/screens/HomeScreen';
import VehiclesScreen from './src/screens/VehiclesScreen';
import VehicleHistoryScreen from './src/screens/VehicleHistoryScreen';

const Stack = createStackNavigator();

const App = () => (
  <NavigationContainer>
    <Stack.Navigator initialRouteName="Home">
      <Stack.Screen name="Home" component={HomeScreen} />
      <Stack.Screen name="Vehicles" component={VehiclesScreen} />
      <Stack.Screen name="Vehicle History" component={VehicleHistoryScreen} />
    </Stack.Navigator>
  </NavigationContainer>
);

export default App;
```

---

## **6. Navigate to Vehicle History from the Vehicles Screen**
Modify `src/screens/VehiclesScreen.js` to allow navigation to the **history screen**.

```javascript
import { useNavigation } from '@react-navigation/native';
import { TouchableOpacity, Text } from 'react-native';

const VehiclesScreen = () => {
  const navigation = useNavigation();

  return (
    <FlatList
      data={vehicles}
      keyExtractor={(item) => item.id.toString()}
      renderItem={({ item }) => (
        <View>
          <Text>{item.name}</Text>
          <TouchableOpacity onPress={() => navigation.navigate('Vehicle History', { vehicleId: item.id })}>
            <Text style={{ color: 'blue' }}>View History</Text>
          </TouchableOpacity>
        </View>
      )}
    />
  );
};
```

---

## **7. Run and Test**
1. **Start Spring Boot Backend**
   ```sh
   mvn spring-boot:run
   ```
2. **Start React Native App**
   ```sh
   npx react-native run-android
   ```
   OR
   ```sh
   npx react-native run-ios
   ```

---

## **8. Expected Behavior**
- Users can **view vehicle history** on a map.
- A **route is drawn** connecting past GPS locations.
- Clicking on a **vehicle in the list** navigates to its history.

---

### **Next Steps**
- Implement **geofencing alerts**.
- Add **time filters** for history retrieval.
- Optimize **battery usage** for real-time tracking.

Would you like me to implement **geofencing alerts** next? 🚀
---
### **Implementing Geofencing Alerts in the Transportation Management App**
Geofencing allows us to **trigger alerts** when a vehicle **enters or exits a predefined area** (e.g., a warehouse, restricted zone, or delivery location).

---

## **1. Steps to Implement Geofencing**
1. **Define geofence areas** in the backend.
2. **Monitor vehicle location** in real-time.
3. **Trigger alerts** when a vehicle enters/exits a geofenced area.
4. **Send push notifications** to the mobile app.

---

## **2. Modify Backend to Support Geofencing**
### **2.1. Define a Geofence Entity**
Create a new `Geofence` entity in **Spring Boot**.

```java
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Geofence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double latitude;
    private double longitude;
    private double radius; // in meters

    @OneToMany(mappedBy = "geofence", cascade = CascadeType.ALL)
    private List<GeofenceAlert> alerts;

    public Geofence() {}

    public Geofence(String name, double latitude, double longitude, double radius) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    // Getters and Setters
}
```

### **2.2. Create a Repository for Geofences**
```java
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GeofenceRepository extends JpaRepository<Geofence, Long> {
    List<Geofence> findAll();
}
```

---

## **3. Create Alerts When Vehicles Enter/Exit a Geofence**
### **3.1. Create an Alert Entity**
```java
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class GeofenceAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "geofence_id")
    private Geofence geofence;

    private String status; // "ENTERED" or "EXITED"
    private LocalDateTime timestamp;

    public GeofenceAlert() {}

    public GeofenceAlert(Vehicle vehicle, Geofence geofence, String status) {
        this.vehicle = vehicle;
        this.geofence = geofence;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
}
```

### **3.2. Create an Alert Repository**
```java
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeofenceAlertRepository extends JpaRepository<GeofenceAlert, Long> {}
```

---

## **4. Implement Geofencing Logic**
Modify `VehicleService.java` to **check geofencing status**.

```java
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GeofenceService {

    private final VehicleRepository vehicleRepository;
    private final GeofenceRepository geofenceRepository;
    private final GeofenceAlertRepository alertRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public GeofenceService(VehicleRepository vehicleRepository, GeofenceRepository geofenceRepository,
                           GeofenceAlertRepository alertRepository, SimpMessagingTemplate messagingTemplate) {
        this.vehicleRepository = vehicleRepository;
        this.geofenceRepository = geofenceRepository;
        this.alertRepository = alertRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public void checkGeofence(Long vehicleId, double latitude, double longitude) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(vehicleId);
        if (optionalVehicle.isPresent()) {
            Vehicle vehicle = optionalVehicle.get();
            List<Geofence> geofences = geofenceRepository.findAll();

            for (Geofence geofence : geofences) {
                double distance = calculateDistance(latitude, longitude, geofence.getLatitude(), geofence.getLongitude());

                boolean isInside = distance <= geofence.getRadius();

                if (isInside && !vehicle.isInsideGeofence()) {
                    vehicle.setInsideGeofence(true);
                    alertRepository.save(new GeofenceAlert(vehicle, geofence, "ENTERED"));
                    messagingTemplate.convertAndSend("/topic/geofence-alerts", "Vehicle " + vehicle.getId() + " ENTERED " + geofence.getName());
                } else if (!isInside && vehicle.isInsideGeofence()) {
                    vehicle.setInsideGeofence(false);
                    alertRepository.save(new GeofenceAlert(vehicle, geofence, "EXITED"));
                    messagingTemplate.convertAndSend("/topic/geofence-alerts", "Vehicle " + vehicle.getId() + " EXITED " + geofence.getName());
                }

                vehicleRepository.save(vehicle);
            }
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371000; // meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}
```

---

## **5. Modify Vehicle Controller to Check Geofences**
Update `VehicleController.java` to check geofences on location update.

```java
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final GeofenceService geofenceService;

    public VehicleController(GeofenceService geofenceService) {
        this.geofenceService = geofenceService;
    }

    @PostMapping("/update-location")
    public void updateVehicleLocation(@RequestBody Vehicle vehicleUpdate) {
        geofenceService.checkGeofence(vehicleUpdate.getId(), vehicleUpdate.getLatitude(), vehicleUpdate.getLongitude());
    }
}
```

---

## **6. Implement Real-Time Alerts in React Native**
### **6.1. Subscribe to Geofence Alerts**
Modify `src/services/websocket.js` to receive geofence alerts.

```javascript
export const subscribeToGeofenceAlerts = (callback) => {
  socketClient.onConnect = () => {
    socketClient.subscribe('/topic/geofence-alerts', (message) => {
      callback(message.body);
    });
  };
};
```

### **6.2. Display Alerts in React Native**
Modify `src/screens/HomeScreen.js` to show alerts.

```javascript
import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { subscribeToGeofenceAlerts } from '../services/websocket';

const HomeScreen = () => {
  const [alertMessage, setAlertMessage] = useState('');

  useEffect(() => {
    subscribeToGeofenceAlerts((message) => {
      setAlertMessage(message);
    });
  }, []);

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Geofence Alerts</Text>
      {alertMessage ? <Text style={styles.alert}>{alertMessage}</Text> : <Text>No alerts</Text>}
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: 'center', alignItems: 'center' },
  title: { fontSize: 24, fontWeight: 'bold' },
  alert: { fontSize: 18, color: 'red', marginTop: 10 }
});

export default HomeScreen;
```

---

## **7. Run and Test**
1. **Start Spring Boot Backend**
   ```sh
   mvn spring-boot:run
   ```
2. **Start React Native App**
   ```sh
   npx react-native run-android
   ```
   OR
   ```sh
   npx react-native run-ios
   ```
3. **Test Alerts**
   - Update a vehicle's location in the database or manually move it in the app.
   - If the vehicle enters/exits a geofenced area, a **real-time alert appears**.

---

## **8. Next Steps**
- Add a **dashboard to manage geofences**.
- Implement **push notifications** for alerts.
- Optimize **battery efficiency** for continuous GPS tracking.

Would you like me to implement **push notifications** for geofence alerts next? 🚀
---
### **Implementing Push Notifications for Geofence Alerts in the Transportation Management App**
To enhance real-time geofencing, we'll implement **push notifications** in **React Native** so users receive alerts even when the app is in the background.

---

## **1. Steps to Implement Push Notifications**
1. **Set up Firebase Cloud Messaging (FCM) for push notifications**.
2. **Integrate Firebase into the React Native app**.
3. **Modify the Spring Boot backend to send push notifications**.
4. **Handle push notifications in the React Native app**.

---

## **2. Set Up Firebase Cloud Messaging (FCM)**
### **2.1. Create a Firebase Project**
1. Go to [Firebase Console](https://console.firebase.google.com/).
2. Create a new project.
3. Navigate to **Project Settings** > **Cloud Messaging**.
4. Generate and **save the Firebase Server Key** (this will be used in the backend).

### **2.2. Add Firebase to React Native**
Install Firebase dependencies:
```sh
npm install @react-native-firebase/app @react-native-firebase/messaging
```
For **iOS**, install pods:
```sh
cd ios && pod install && cd ..
```

---

## **3. Configure Firebase in React Native**
### **3.1. Modify `android/app/build.gradle`**
Add the following:
```gradle
dependencies {
    implementation platform('com.google.firebase:firebase-bom:32.1.0')
    implementation 'com.google.firebase:firebase-messaging'
}
```

### **3.2. Modify `android/app/src/main/AndroidManifest.xml`**
Add permissions for FCM:
```xml
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
<uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>
```
---

## **4. Implement Push Notifications in React Native**
### **4.1. Request Notification Permissions**
Modify `src/services/notifications.js`:

```javascript
import messaging from '@react-native-firebase/messaging';
import { Alert } from 'react-native';

export async function requestUserPermission() {
  const authStatus = await messaging().requestPermission();
  const enabled =
    authStatus === messaging.AuthorizationStatus.AUTHORIZED ||
    authStatus === messaging.AuthorizationStatus.PROVISIONAL;

  if (enabled) {
    console.log('Notification permission granted.');
    getFCMToken();
  }
}

async function getFCMToken() {
  try {
    const token = await messaging().getToken();
    console.log('FCM Token:', token);
    return token;
  } catch (error) {
    console.error('Error getting FCM token:', error);
  }
}

export function listenForNotifications() {
  messaging().onMessage(async (remoteMessage) => {
    Alert.alert('Geofence Alert', remoteMessage.notification.body);
  });
}
```

### **4.2. Call Notification Setup in `App.js`**
Modify `App.js`:

```javascript
import React, { useEffect } from 'react';
import { requestUserPermission, listenForNotifications } from './src/services/notifications';

const App = () => {
  useEffect(() => {
    requestUserPermission();
    listenForNotifications();
  }, []);

  return <NavigationContainer>{/* Screens here */}</NavigationContainer>;
};

export default App;
```

---

## **5. Modify Spring Boot Backend to Send Notifications**
### **5.1. Add Firebase Admin SDK**
Add the Firebase SDK to `pom.xml`:
```xml
<dependency>
    <groupId>com.google.firebase</groupId>
    <artifactId>firebase-admin</artifactId>
    <version>9.2.0</version>
</dependency>
```

### **5.2. Initialize Firebase in Spring Boot**
Download the **Firebase Service Account JSON** from **Firebase Console**, then place it in your Spring Boot project (`src/main/resources/firebase-service-account.json`).

Modify `FirebaseMessagingService.java`:
```java
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;

import org.springframework.stereotype.Service;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class FirebaseMessagingService {

    public FirebaseMessagingService() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream("src/main/resources/firebase-service-account.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }

    public void sendPushNotification(String token, String title, String body) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        FirebaseMessaging.getInstance().send(message);
    }
}
```

### **5.3. Modify Geofence Service to Send Push Notifications**
Modify `GeofenceService.java`:

```java
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.stereotype.Service;

@Service
public class GeofenceService {

    private final FirebaseMessagingService firebaseMessagingService;
    private final GeofenceRepository geofenceRepository;
    private final VehicleRepository vehicleRepository;

    public GeofenceService(FirebaseMessagingService firebaseMessagingService, 
                           GeofenceRepository geofenceRepository, VehicleRepository vehicleRepository) {
        this.firebaseMessagingService = firebaseMessagingService;
        this.geofenceRepository = geofenceRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public void checkGeofence(Long vehicleId, double latitude, double longitude, String fcmToken) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElse(null);
        if (vehicle == null) return;

        for (Geofence geofence : geofenceRepository.findAll()) {
            double distance = calculateDistance(latitude, longitude, geofence.getLatitude(), geofence.getLongitude());
            boolean isInside = distance <= geofence.getRadius();

            if (isInside && !vehicle.isInsideGeofence()) {
                vehicle.setInsideGeofence(true);
                sendAlert(fcmToken, "Geofence Alert", "Vehicle entered " + geofence.getName());
            } else if (!isInside && vehicle.isInsideGeofence()) {
                vehicle.setInsideGeofence(false);
                sendAlert(fcmToken, "Geofence Alert", "Vehicle exited " + geofence.getName());
            }

            vehicleRepository.save(vehicle);
        }
    }

    private void sendAlert(String fcmToken, String title, String body) {
        try {
            firebaseMessagingService.sendPushNotification(fcmToken, title, body);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}
```

---

## **6. Run and Test**
1. **Start Spring Boot Backend**
   ```sh
   mvn spring-boot:run
   ```
2. **Start React Native App**
   ```sh
   npx react-native run-android
   ```
   OR
   ```sh
   npx react-native run-ios
   ```
3. **Test Push Notifications**
   - Move a vehicle **inside or outside** a geofenced area.
   - The **Firebase notification** will be sent **to the mobile app**.

---

## **7. Expected Behavior**
- When a vehicle **enters or exits a geofence**, the backend:
  - Sends a **WebSocket alert**.
  - Sends a **push notification** to the user.
- The **React Native app displays the alert**.

---

## **8. Next Steps**
- Add a **geofence management dashboard**.
- Implement **push notification settings (mute, snooze, etc.)**.
- Optimize **battery usage for tracking**.

Would you like me to implement **push notification settings** next? 🚀
---
### **Implementing Push Notification Settings in the Transportation Management App**
To improve the user experience, we'll implement **notification settings**, allowing users to:
1. **Enable or disable notifications**.
2. **Mute notifications for a specific time period**.
3. **Customize notification preferences** (e.g., alerts for entry only, exit only, or both).

---

## **1. Steps to Implement Notification Settings**
1. **Store user preferences locally using AsyncStorage**.
2. **Update React Native UI to manage notification settings**.
3. **Modify Firebase push notification handling** based on user preferences**.

---

## **2. Install AsyncStorage for Local Storage**
We'll use AsyncStorage to store user preferences.

```sh
npm install @react-native-async-storage/async-storage
```

---

## **3. Create Notification Settings Service**
Modify `src/services/notificationSettings.js` to manage notification preferences.

```javascript
import AsyncStorage from '@react-native-async-storage/async-storage';

const NOTIFICATION_PREF_KEY = 'notification_preferences';

export const saveNotificationPreferences = async (preferences) => {
  try {
    await AsyncStorage.setItem(NOTIFICATION_PREF_KEY, JSON.stringify(preferences));
  } catch (error) {
    console.error('Error saving notification preferences:', error);
  }
};

export const getNotificationPreferences = async () => {
  try {
    const storedPreferences = await AsyncStorage.getItem(NOTIFICATION_PREF_KEY);
    return storedPreferences ? JSON.parse(storedPreferences) : { enabled: true, muteUntil: null };
  } catch (error) {
    console.error('Error retrieving notification preferences:', error);
    return { enabled: true, muteUntil: null };
  }
};
```

---

## **4. Create Notification Settings Screen**
Modify `src/screens/NotificationSettingsScreen.js` to allow users to update their preferences.

```javascript
import React, { useEffect, useState } from 'react';
import { View, Text, Switch, Button, StyleSheet, Alert } from 'react-native';
import { saveNotificationPreferences, getNotificationPreferences } from '../services/notificationSettings';

const NotificationSettingsScreen = () => {
  const [enabled, setEnabled] = useState(true);
  const [muteUntil, setMuteUntil] = useState(null);

  useEffect(() => {
    loadPreferences();
  }, []);

  const loadPreferences = async () => {
    const preferences = await getNotificationPreferences();
    setEnabled(preferences.enabled);
    setMuteUntil(preferences.muteUntil);
  };

  const toggleNotifications = async () => {
    const newPreferences = { enabled: !enabled, muteUntil: null };
    setEnabled(newPreferences.enabled);
    setMuteUntil(null);
    await saveNotificationPreferences(newPreferences);
  };

  const muteFor30Minutes = async () => {
    const muteUntilTime = new Date().getTime() + 30 * 60 * 1000; // 30 minutes from now
    setMuteUntil(muteUntilTime);
    await saveNotificationPreferences({ enabled, muteUntil: muteUntilTime });
    Alert.alert('Muted', 'Notifications will be muted for 30 minutes.');
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Notification Settings</Text>
      
      <View style={styles.setting}>
        <Text>Enable Notifications</Text>
        <Switch value={enabled} onValueChange={toggleNotifications} />
      </View>

      <Button title="Mute for 30 Minutes" onPress={muteFor30Minutes} disabled={!enabled} />
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: 'center', padding: 20 },
  title: { fontSize: 24, fontWeight: 'bold', textAlign: 'center', marginBottom: 20 },
  setting: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 20 }
});

export default NotificationSettingsScreen;
```

---

## **5. Modify App Navigation to Include Settings Screen**
Modify `App.js`:

```javascript
import NotificationSettingsScreen from './src/screens/NotificationSettingsScreen';

<Stack.Screen name="Notification Settings" component={NotificationSettingsScreen} />
```

---

## **6. Modify Push Notification Handler to Respect User Preferences**
Update `src/services/notifications.js`:

```javascript
import messaging from '@react-native-firebase/messaging';
import { Alert } from 'react-native';
import { getNotificationPreferences } from './notificationSettings';

export async function handleIncomingNotification(remoteMessage) {
  const preferences = await getNotificationPreferences();

  if (!preferences.enabled) return;

  if (preferences.muteUntil && new Date().getTime() < preferences.muteUntil) {
    console.log('Notifications muted, ignoring...');
    return;
  }

  Alert.alert(remoteMessage.notification.title, remoteMessage.notification.body);
}

export function listenForNotifications() {
  messaging().onMessage(async (remoteMessage) => {
    await handleIncomingNotification(remoteMessage);
  });
}
```

---

## **7. Modify Backend to Respect User Preferences**
Modify `GeofenceService.java` to avoid sending notifications if muted.

```java
public void checkGeofence(Long vehicleId, double latitude, double longitude, String fcmToken, boolean notificationsEnabled) {
    if (!notificationsEnabled) return;

    Vehicle vehicle = vehicleRepository.findById(vehicleId).orElse(null);
    if (vehicle == null) return;

    for (Geofence geofence : geofenceRepository.findAll()) {
        double distance = calculateDistance(latitude, longitude, geofence.getLatitude(), geofence.getLongitude());
        boolean isInside = distance <= geofence.getRadius();

        if (isInside && !vehicle.isInsideGeofence()) {
            vehicle.setInsideGeofence(true);
            sendAlert(fcmToken, "Geofence Alert", "Vehicle entered " + geofence.getName());
        } else if (!isInside && vehicle.isInsideGeofence()) {
            vehicle.setInsideGeofence(false);
            sendAlert(fcmToken, "Geofence Alert", "Vehicle exited " + geofence.getName());
        }

        vehicleRepository.save(vehicle);
    }
}
```

---

## **8. Run and Test**
1. **Start Spring Boot Backend**
   ```sh
   mvn spring-boot:run
   ```
2. **Start React Native App**
   ```sh
   npx react-native run-android
   ```
   OR
   ```sh
   npx react-native run-ios
   ```
3. **Test Notifications**
   - **Enable/disable notifications** in settings.
   - **Mute notifications for 30 minutes**.
   - **Check if push notifications are skipped** when muted.

---

## **9. Expected Behavior**
✅ Users can enable/disable notifications.  
✅ Notifications **don't appear** when muted.  
✅ Users receive alerts when a vehicle **enters/exits a geofence** if notifications are **enabled**.  

---

## **10. Next Steps**
- Add **custom geofence rules** (e.g., "Notify only when vehicle exits").
- Implement a **history of past alerts**.
- Optimize **battery usage** for GPS tracking.

Would you like me to implement **custom geofence rules** next? 🚀
---
### **Implementing Custom Geofence Rules in the Transportation Management App**
To improve geofencing alerts, we will allow users to:
1. **Set custom geofence rules** (e.g., receive alerts only for entry, exit, or both).
2. **Modify the backend to store and enforce rules**.
3. **Update the React Native UI** to allow users to set preferences.
4. **Ensure notifications are sent based on user-defined rules**.

---

## **1. Modify Database to Store Geofence Rules**
### **1.1. Update Geofence Entity to Store Rules**
Modify the `Geofence` entity in **Spring Boot** to include user-defined rules.

```java
import jakarta.persistence.*;

@Entity
public class Geofence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double latitude;
    private double longitude;
    private double radius; // in meters
    private boolean notifyOnEntry;
    private boolean notifyOnExit;

    public Geofence() {}

    public Geofence(String name, double latitude, double longitude, double radius, boolean notifyOnEntry, boolean notifyOnExit) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.notifyOnEntry = notifyOnEntry;
        this.notifyOnExit = notifyOnExit;
    }

    // Getters and Setters
}
```

### **1.2. Modify Geofence Repository**
```java
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeofenceRepository extends JpaRepository<Geofence, Long> {}
```

---

## **2. Modify Backend to Enforce Geofence Rules**
Modify `GeofenceService.java` to check the **user-defined geofence rules** before sending alerts.

```java
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.stereotype.Service;

@Service
public class GeofenceService {

    private final FirebaseMessagingService firebaseMessagingService;
    private final GeofenceRepository geofenceRepository;
    private final VehicleRepository vehicleRepository;

    public GeofenceService(FirebaseMessagingService firebaseMessagingService, 
                           GeofenceRepository geofenceRepository, VehicleRepository vehicleRepository) {
        this.firebaseMessagingService = firebaseMessagingService;
        this.geofenceRepository = geofenceRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public void checkGeofence(Long vehicleId, double latitude, double longitude, String fcmToken) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElse(null);
        if (vehicle == null) return;

        for (Geofence geofence : geofenceRepository.findAll()) {
            double distance = calculateDistance(latitude, longitude, geofence.getLatitude(), geofence.getLongitude());
            boolean isInside = distance <= geofence.getRadius();

            if (isInside && !vehicle.isInsideGeofence() && geofence.isNotifyOnEntry()) {
                vehicle.setInsideGeofence(true);
                sendAlert(fcmToken, "Geofence Alert", "Vehicle entered " + geofence.getName());
            } else if (!isInside && vehicle.isInsideGeofence() && geofence.isNotifyOnExit()) {
                vehicle.setInsideGeofence(false);
                sendAlert(fcmToken, "Geofence Alert", "Vehicle exited " + geofence.getName());
            }

            vehicleRepository.save(vehicle);
        }
    }

    private void sendAlert(String fcmToken, String title, String body) {
        try {
            firebaseMessagingService.sendPushNotification(fcmToken, title, body);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}
```

---

## **3. Create an API to Manage Geofence Rules**
Modify `GeofenceController.java` to allow users to update geofence rules.

```java
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/geofences")
public class GeofenceController {

    private final GeofenceRepository geofenceRepository;

    public GeofenceController(GeofenceRepository geofenceRepository) {
        this.geofenceRepository = geofenceRepository;
    }

    @GetMapping
    public List<Geofence> getAllGeofences() {
        return geofenceRepository.findAll();
    }

    @PostMapping("/update-rules")
    public Geofence updateGeofenceRules(@RequestBody Geofence updatedGeofence) {
        Geofence geofence = geofenceRepository.findById(updatedGeofence.getId()).orElse(null);
        if (geofence != null) {
            geofence.setNotifyOnEntry(updatedGeofence.isNotifyOnEntry());
            geofence.setNotifyOnExit(updatedGeofence.isNotifyOnExit());
            return geofenceRepository.save(geofence);
        }
        return null;
    }
}
```

---

## **4. Modify React Native to Allow Users to Set Rules**
### **4.1. Add API Calls to Update Geofence Rules**
Modify `src/services/api.js`:

```javascript
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/geofences';

export const getGeofences = async () => {
  try {
    const response = await axios.get(API_BASE_URL);
    return response.data;
  } catch (error) {
    console.error('Error fetching geofences:', error);
    return [];
  }
};

export const updateGeofenceRules = async (geofence) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/update-rules`, geofence);
    return response.data;
  } catch (error) {
    console.error('Error updating geofence rules:', error);
    return null;
  }
};
```

---

### **4.2. Create a Geofence Management Screen**
Create `src/screens/GeofenceSettingsScreen.js`:

```javascript
import React, { useEffect, useState } from 'react';
import { View, Text, Switch, FlatList, Button, StyleSheet } from 'react-native';
import { getGeofences, updateGeofenceRules } from '../services/api';

const GeofenceSettingsScreen = () => {
  const [geofences, setGeofences] = useState([]);

  useEffect(() => {
    fetchGeofences();
  }, []);

  const fetchGeofences = async () => {
    const data = await getGeofences();
    setGeofences(data);
  };

  const toggleEntryAlert = async (geofence) => {
    geofence.notifyOnEntry = !geofence.notifyOnEntry;
    await updateGeofenceRules(geofence);
    fetchGeofences();
  };

  const toggleExitAlert = async (geofence) => {
    geofence.notifyOnExit = !geofence.notifyOnExit;
    await updateGeofenceRules(geofence);
    fetchGeofences();
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Geofence Settings</Text>
      <FlatList
        data={geofences}
        keyExtractor={(item) => item.id.toString()}
        renderItem={({ item }) => (
          <View style={styles.geofence}>
            <Text>{item.name}</Text>
            <View style={styles.switchRow}>
              <Text>Alert on Entry</Text>
              <Switch value={item.notifyOnEntry} onValueChange={() => toggleEntryAlert(item)} />
            </View>
            <View style={styles.switchRow}>
              <Text>Alert on Exit</Text>
              <Switch value={item.notifyOnExit} onValueChange={() => toggleExitAlert(item)} />
            </View>
          </View>
        )}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, padding: 20 },
  title: { fontSize: 24, fontWeight: 'bold', textAlign: 'center', marginBottom: 20 },
  geofence: { padding: 10, borderBottomWidth: 1 },
  switchRow: { flexDirection: 'row', justifyContent: 'space-between', marginVertical: 5 }
});

export default GeofenceSettingsScreen;
```

---

## **5. Update Navigation**
Modify `App.js`:

```javascript
import GeofenceSettingsScreen from './src/screens/GeofenceSettingsScreen';

<Stack.Screen name="Geofence Settings" component={GeofenceSettingsScreen} />
```

---

## **Next Steps**
- Allow users to **add custom geofences**.
- Implement a **history of geofence alerts**.

Would you like me to implement **custom geofence creation** next? 🚀
---
### **Implementing Custom Geofence Creation in the Transportation Management App**
To allow users to **add custom geofences**, we will:
1. **Create a UI for adding geofences** (map-based or manual input).
2. **Modify the backend to save user-created geofences**.
3. **Update the React Native app to send geofence data**.

---

## **1. Modify Backend to Support Geofence Creation**
### **1.1. Update `GeofenceController.java` to Accept New Geofences**
Modify `GeofenceController.java` to handle **POST requests** for adding new geofences.

```java
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/geofences")
public class GeofenceController {

    private final GeofenceRepository geofenceRepository;

    public GeofenceController(GeofenceRepository geofenceRepository) {
        this.geofenceRepository = geofenceRepository;
    }

    @GetMapping
    public List<Geofence> getAllGeofences() {
        return geofenceRepository.findAll();
    }

    @PostMapping("/create")
    public Geofence createGeofence(@RequestBody Geofence newGeofence) {
        return geofenceRepository.save(newGeofence);
    }

    @DeleteMapping("/{id}")
    public void deleteGeofence(@PathVariable Long id) {
        geofenceRepository.deleteById(id);
    }
}
```

---

## **2. Update API Service in React Native**
Modify `src/services/api.js` to support adding geofences.

```javascript
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/geofences';

export const createGeofence = async (geofence) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/create`, geofence);
    return response.data;
  } catch (error) {
    console.error('Error creating geofence:', error);
    return null;
  }
};

export const deleteGeofence = async (id) => {
  try {
    await axios.delete(`${API_BASE_URL}/${id}`);
  } catch (error) {
    console.error('Error deleting geofence:', error);
  }
};
```

---

## **3. Create a UI for Adding Geofences**
Create `src/screens/AddGeofenceScreen.js`:

```javascript
import React, { useState } from 'react';
import { View, Text, TextInput, Button, Alert, StyleSheet } from 'react-native';
import { createGeofence } from '../services/api';
import MapView, { Marker } from 'react-native-maps';

const AddGeofenceScreen = ({ navigation }) => {
  const [name, setName] = useState('');
  const [radius, setRadius] = useState('');
  const [coordinate, setCoordinate] = useState(null);

  const handleMapPress = (event) => {
    setCoordinate(event.nativeEvent.coordinate);
  };

  const handleSubmit = async () => {
    if (!name || !radius || !coordinate) {
      Alert.alert('Error', 'Please enter all fields and select a location.');
      return;
    }

    const newGeofence = {
      name,
      latitude: coordinate.latitude,
      longitude: coordinate.longitude,
      radius: parseFloat(radius),
      notifyOnEntry: true,
      notifyOnExit: true
    };

    const result = await createGeofence(newGeofence);
    if (result) {
      Alert.alert('Success', 'Geofence added successfully!');
      navigation.goBack();
    } else {
      Alert.alert('Error', 'Failed to add geofence.');
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Add Geofence</Text>
      <TextInput style={styles.input} placeholder="Geofence Name" onChangeText={setName} />
      <TextInput style={styles.input} placeholder="Radius (meters)" keyboardType="numeric" onChangeText={setRadius} />
      
      <MapView style={styles.map} onPress={handleMapPress}>
        {coordinate && <Marker coordinate={coordinate} />}
      </MapView>

      <Button title="Add Geofence" onPress={handleSubmit} />
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, padding: 20 },
  title: { fontSize: 24, fontWeight: 'bold', textAlign: 'center', marginBottom: 20 },
  input: { borderWidth: 1, padding: 10, marginBottom: 10 },
  map: { flex: 1, marginBottom: 10 }
});

export default AddGeofenceScreen;
```

---

## **4. Update Geofence Settings to Manage Geofences**
Modify `src/screens/GeofenceSettingsScreen.js`:

```javascript
import React, { useEffect, useState } from 'react';
import { View, Text, Switch, FlatList, Button, TouchableOpacity, StyleSheet, Alert } from 'react-native';
import { getGeofences, updateGeofenceRules, deleteGeofence } from '../services/api';

const GeofenceSettingsScreen = ({ navigation }) => {
  const [geofences, setGeofences] = useState([]);

  useEffect(() => {
    fetchGeofences();
  }, []);

  const fetchGeofences = async () => {
    const data = await getGeofences();
    setGeofences(data);
  };

  const toggleEntryAlert = async (geofence) => {
    geofence.notifyOnEntry = !geofence.notifyOnEntry;
    await updateGeofenceRules(geofence);
    fetchGeofences();
  };

  const toggleExitAlert = async (geofence) => {
    geofence.notifyOnExit = !geofence.notifyOnExit;
    await updateGeofenceRules(geofence);
    fetchGeofences();
  };

  const handleDelete = async (id) => {
    await deleteGeofence(id);
    fetchGeofences();
    Alert.alert('Deleted', 'Geofence removed successfully.');
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Geofence Settings</Text>

      <Button title="Add New Geofence" onPress={() => navigation.navigate('Add Geofence')} />

      <FlatList
        data={geofences}
        keyExtractor={(item) => item.id.toString()}
        renderItem={({ item }) => (
          <View style={styles.geofence}>
            <Text>{item.name}</Text>
            <View style={styles.switchRow}>
              <Text>Alert on Entry</Text>
              <Switch value={item.notifyOnEntry} onValueChange={() => toggleEntryAlert(item)} />
            </View>
            <View style={styles.switchRow}>
              <Text>Alert on Exit</Text>
              <Switch value={item.notifyOnExit} onValueChange={() => toggleExitAlert(item)} />
            </View>
            <TouchableOpacity onPress={() => handleDelete(item.id)}>
              <Text style={styles.deleteText}>Delete</Text>
            </TouchableOpacity>
          </View>
        )}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, padding: 20 },
  title: { fontSize: 24, fontWeight: 'bold', textAlign: 'center', marginBottom: 20 },
  geofence: { padding: 10, borderBottomWidth: 1 },
  switchRow: { flexDirection: 'row', justifyContent: 'space-between', marginVertical: 5 },
  deleteText: { color: 'red', textAlign: 'center', marginTop: 5 }
});

export default GeofenceSettingsScreen;
```

---

## **5. Update Navigation**
Modify `App.js`:

```javascript
import AddGeofenceScreen from './src/screens/AddGeofenceScreen';

<Stack.Screen name="Geofence Settings" component={GeofenceSettingsScreen} />
<Stack.Screen name="Add Geofence" component={AddGeofenceScreen} />
```

---

## **6. Run and Test**
1. **Start Spring Boot Backend**
   ```sh
   mvn spring-boot:run
   ```
2. **Start React Native App**
   ```sh
   npx react-native run-android
   ```
   OR
   ```sh
   npx react-native run-ios
   ```
3. **Test Geofence Management**
   - **Add new geofences** via the map UI.
   - **Modify alerts (entry/exit notifications)**.
   - **Delete geofences** when no longer needed.

---

## **7. Next Steps**
- **Implement a Geofence Alert History screen**.
- **Optimize battery usage** for continuous location tracking.

Would you like me to implement **Geofence Alert History** next? 🚀
---
### **Implementing Geofence Alert History in the Transportation Management App**
To allow users to **view past geofence alerts**, we will:
1. **Create a database model for geofence alert history**.
2. **Modify the backend to store and retrieve alert history**.
3. **Create a UI in React Native to display past alerts**.

---

## **1. Modify Backend to Store Alert History**
### **1.1. Create `GeofenceAlertHistory` Entity**
Modify `GeofenceAlert.java` to track geofence event history.

```java
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class GeofenceAlertHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "geofence_id")
    private Geofence geofence;

    private String event; // "ENTERED" or "EXITED"
    private LocalDateTime timestamp;

    public GeofenceAlertHistory() {}

    public GeofenceAlertHistory(Vehicle vehicle, Geofence geofence, String event) {
        this.vehicle = vehicle;
        this.geofence = geofence;
        this.event = event;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
}
```

---

### **1.2. Create `GeofenceAlertHistoryRepository.java`**
```java
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GeofenceAlertHistoryRepository extends JpaRepository<GeofenceAlertHistory, Long> {
    List<GeofenceAlertHistory> findByVehicleIdOrderByTimestampDesc(Long vehicleId);
}
```

---

### **1.3. Modify `GeofenceService.java` to Save Alert History**
Update `GeofenceService.java` to log geofence entry/exit events.

```java
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GeofenceService {

    private final GeofenceRepository geofenceRepository;
    private final VehicleRepository vehicleRepository;
    private final GeofenceAlertHistoryRepository alertHistoryRepository;
    private final FirebaseMessagingService firebaseMessagingService;

    public GeofenceService(GeofenceRepository geofenceRepository, VehicleRepository vehicleRepository,
                           GeofenceAlertHistoryRepository alertHistoryRepository, FirebaseMessagingService firebaseMessagingService) {
        this.geofenceRepository = geofenceRepository;
        this.vehicleRepository = vehicleRepository;
        this.alertHistoryRepository = alertHistoryRepository;
        this.firebaseMessagingService = firebaseMessagingService;
    }

    public void checkGeofence(Long vehicleId, double latitude, double longitude, String fcmToken) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElse(null);
        if (vehicle == null) return;

        for (Geofence geofence : geofenceRepository.findAll()) {
            double distance = calculateDistance(latitude, longitude, geofence.getLatitude(), geofence.getLongitude());
            boolean isInside = distance <= geofence.getRadius();

            if (isInside && !vehicle.isInsideGeofence() && geofence.isNotifyOnEntry()) {
                vehicle.setInsideGeofence(true);
                saveAlert(vehicle, geofence, "ENTERED", fcmToken);
            } else if (!isInside && vehicle.isInsideGeofence() && geofence.isNotifyOnExit()) {
                vehicle.setInsideGeofence(false);
                saveAlert(vehicle, geofence, "EXITED", fcmToken);
            }

            vehicleRepository.save(vehicle);
        }
    }

    private void saveAlert(Vehicle vehicle, Geofence geofence, String event, String fcmToken) {
        GeofenceAlertHistory alert = new GeofenceAlertHistory(vehicle, geofence, event);
        alertHistoryRepository.save(alert);
        
        try {
            firebaseMessagingService.sendPushNotification(fcmToken, "Geofence Alert", 
                "Vehicle " + event + " " + geofence.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}
```

---

### **1.4. Create API to Retrieve Alert History**
Modify `GeofenceController.java`:

```java
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/geofences")
public class GeofenceController {

    private final GeofenceAlertHistoryRepository alertHistoryRepository;

    public GeofenceController(GeofenceAlertHistoryRepository alertHistoryRepository) {
        this.alertHistoryRepository = alertHistoryRepository;
    }

    @GetMapping("/{vehicleId}/history")
    public List<GeofenceAlertHistory> getAlertHistory(@PathVariable Long vehicleId) {
        return alertHistoryRepository.findByVehicleIdOrderByTimestampDesc(vehicleId);
    }
}
```

---

## **2. Update API Service in React Native**
Modify `src/services/api.js` to fetch alert history.

```javascript
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/geofences';

export const getGeofenceHistory = async (vehicleId) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${vehicleId}/history`);
    return response.data;
  } catch (error) {
    console.error('Error fetching geofence history:', error);
    return [];
  }
};
```

---

## **3. Create UI for Alert History**
Create `src/screens/GeofenceHistoryScreen.js`:

```javascript
import React, { useEffect, useState } from 'react';
import { View, Text, FlatList, StyleSheet, ActivityIndicator } from 'react-native';
import { getGeofenceHistory } from '../services/api';

const GeofenceHistoryScreen = ({ route }) => {
  const { vehicleId } = route.params;
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchHistory();
  }, []);

  const fetchHistory = async () => {
    const data = await getGeofenceHistory(vehicleId);
    setHistory(data);
    setLoading(false);
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Geofence Alert History</Text>

      {loading ? (
        <ActivityIndicator size="large" color="#0000ff" />
      ) : (
        <FlatList
          data={history}
          keyExtractor={(item) => item.id.toString()}
          renderItem={({ item }) => (
            <View style={styles.alertItem}>
              <Text style={styles.event}>{item.event} {item.geofence.name}</Text>
              <Text style={styles.timestamp}>{new Date(item.timestamp).toLocaleString()}</Text>
            </View>
          )}
        />
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, padding: 20 },
  title: { fontSize: 24, fontWeight: 'bold', textAlign: 'center', marginBottom: 20 },
  alertItem: { padding: 10, borderBottomWidth: 1 },
  event: { fontSize: 18, fontWeight: 'bold' },
  timestamp: { color: 'gray' }
});

export default GeofenceHistoryScreen;
```

---

## **4. Update Navigation**
Modify `App.js`:

```javascript
import GeofenceHistoryScreen from './src/screens/GeofenceHistoryScreen';

<Stack.Screen name="Geofence History" component={GeofenceHistoryScreen} />
```

---

## **5. Add Navigation to History from the Vehicles Screen**
Modify `src/screens/VehiclesScreen.js`:

```javascript
import { useNavigation } from '@react-navigation/native';
import { TouchableOpacity, Text } from 'react-native';

const VehiclesScreen = () => {
  const navigation = useNavigation();

  return (
    <FlatList
      data={vehicles}
      keyExtractor={(item) => item.id.toString()}
      renderItem={({ item }) => (
        <View>
          <Text>{item.name}</Text>
          <TouchableOpacity onPress={() => navigation.navigate('Geofence History', { vehicleId: item.id })}>
            <Text style={{ color: 'blue' }}>View Alert History</Text>
          </TouchableOpacity>
        </View>
      )}
    />
  );
};
```

---

## **Next Steps**
- Optimize **real-time tracking efficiency**.
- Add **exporting features (CSV, PDF)** for geofence history.

Would you like me to implement **data export for history logs** next? 🚀
---
### **Implementing Data Export for Geofence History in the Transportation Management App**
To enable users to **export geofence alert history**, we will:
1. **Modify the backend to support CSV and PDF export**.
2. **Add API endpoints for exporting data**.
3. **Create UI in React Native to trigger exports**.
4. **Generate downloadable CSV and PDF reports**.

---

## **1. Modify Backend to Support CSV and PDF Export**
### **1.1. Add Dependencies for PDF and CSV in `pom.xml`**
```xml
<dependency>
    <groupId>com.opencsv</groupId>
    <artifactId>opencsv</artifactId>
    <version>5.6</version>
</dependency>

<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itext7-core</artifactId>
    <version>7.1.15</version>
</dependency>
```

---

### **1.2. Create CSV Export Service**
Modify `GeofenceExportService.java` to generate a **CSV file**.

```java
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;
import java.io.StringWriter;
import java.util.List;

@Service
public class GeofenceExportService {

    private final GeofenceAlertHistoryRepository alertHistoryRepository;

    public GeofenceExportService(GeofenceAlertHistoryRepository alertHistoryRepository) {
        this.alertHistoryRepository = alertHistoryRepository;
    }

    public String generateCSV(Long vehicleId) {
        List<GeofenceAlertHistory> historyList = alertHistoryRepository.findByVehicleIdOrderByTimestampDesc(vehicleId);
        StringWriter writer = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer);

        String[] header = { "Vehicle ID", "Geofence", "Event", "Timestamp" };
        csvWriter.writeNext(header);

        for (GeofenceAlertHistory history : historyList) {
            String[] row = {
                String.valueOf(history.getVehicle().getId()),
                history.getGeofence().getName(),
                history.getEvent(),
                history.getTimestamp().toString()
            };
            csvWriter.writeNext(row);
        }

        csvWriter.close();
        return writer.toString();
    }
}
```

---

### **1.3. Create PDF Export Service**
Modify `GeofenceExportService.java` to include **PDF generation**.

```java
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import java.io.ByteArrayOutputStream;
import java.util.List;

public byte[] generatePDF(Long vehicleId) {
    List<GeofenceAlertHistory> historyList = alertHistoryRepository.findByVehicleIdOrderByTimestampDesc(vehicleId);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PdfWriter writer = new PdfWriter(out);
    PdfDocument pdf = new PdfDocument(writer);
    Document document = new Document(pdf);

    document.add(new Paragraph("Geofence Alert History").setBold());

    Table table = new Table(4);
    table.addCell(new Cell().add(new Paragraph("Vehicle ID")));
    table.addCell(new Cell().add(new Paragraph("Geofence")));
    table.addCell(new Cell().add(new Paragraph("Event")));
    table.addCell(new Cell().add(new Paragraph("Timestamp")));

    for (GeofenceAlertHistory history : historyList) {
        table.addCell(new Cell().add(new Paragraph(String.valueOf(history.getVehicle().getId()))));
        table.addCell(new Cell().add(new Paragraph(history.getGeofence().getName())));
        table.addCell(new Cell().add(new Paragraph(history.getEvent())));
        table.addCell(new Cell().add(new Paragraph(history.getTimestamp().toString())));
    }

    document.add(table);
    document.close();
    return out.toByteArray();
}
```

---

### **1.4. Create API Endpoints for CSV and PDF Download**
Modify `GeofenceController.java`:

```java
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/geofences")
public class GeofenceController {

    private final GeofenceExportService exportService;

    public GeofenceController(GeofenceExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping("/{vehicleId}/export/csv")
    public ResponseEntity<byte[]> exportCSV(@PathVariable Long vehicleId) {
        String csvData = exportService.generateCSV(vehicleId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=geofence_history.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(csvData.getBytes());
    }

    @GetMapping("/{vehicleId}/export/pdf")
    public ResponseEntity<byte[]> exportPDF(@PathVariable Long vehicleId) {
        byte[] pdfData = exportService.generatePDF(vehicleId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=geofence_history.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfData);
    }
}
```

---

## **2. Update API Service in React Native**
Modify `src/services/api.js`:

```javascript
import axios from 'axios';
import { Alert } from 'react-native';
import FileSystem from 'react-native-fs';

const API_BASE_URL = 'http://localhost:8080/api/geofences';

export const downloadCSV = async (vehicleId) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${vehicleId}/export/csv`, { responseType: 'blob' });
    const path = `${FileSystem.DownloadDirectoryPath}/geofence_history.csv`;
    await FileSystem.writeFile(path, response.data, 'utf8');
    Alert.alert('Success', `CSV saved to ${path}`);
  } catch (error) {
    console.error('Error downloading CSV:', error);
    Alert.alert('Error', 'Failed to download CSV.');
  }
};

export const downloadPDF = async (vehicleId) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${vehicleId}/export/pdf`, { responseType: 'blob' });
    const path = `${FileSystem.DownloadDirectoryPath}/geofence_history.pdf`;
    await FileSystem.writeFile(path, response.data, 'base64');
    Alert.alert('Success', `PDF saved to ${path}`);
  } catch (error) {
    console.error('Error downloading PDF:', error);
    Alert.alert('Error', 'Failed to download PDF.');
  }
};
```

---

## **3. Modify UI for Export Options**
Modify `src/screens/GeofenceHistoryScreen.js`:

```javascript
import React, { useEffect, useState } from 'react';
import { View, Text, FlatList, Button, StyleSheet, ActivityIndicator } from 'react-native';
import { getGeofenceHistory, downloadCSV, downloadPDF } from '../services/api';

const GeofenceHistoryScreen = ({ route }) => {
  const { vehicleId } = route.params;
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchHistory();
  }, []);

  const fetchHistory = async () => {
    const data = await getGeofenceHistory(vehicleId);
    setHistory(data);
    setLoading(false);
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Geofence Alert History</Text>

      <Button title="Export as CSV" onPress={() => downloadCSV(vehicleId)} />
      <Button title="Export as PDF" onPress={() => downloadPDF(vehicleId)} />

      {loading ? (
        <ActivityIndicator size="large" color="#0000ff" />
      ) : (
        <FlatList
          data={history}
          keyExtractor={(item) => item.id.toString()}
          renderItem={({ item }) => (
            <View style={styles.alertItem}>
              <Text style={styles.event}>{item.event} {item.geofence.name}</Text>
              <Text style={styles.timestamp}>{new Date(item.timestamp).toLocaleString()}</Text>
            </View>
          )}
        />
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, padding: 20 },
  title: { fontSize: 24, fontWeight: 'bold', textAlign: 'center', marginBottom: 20 },
  alertItem: { padding: 10, borderBottomWidth: 1 },
  event: { fontSize: 18, fontWeight: 'bold' },
  timestamp: { color: 'gray' }
});

export default GeofenceHistoryScreen;
```

---

## **Next Steps**
- Add **email support** to send reports.
- Implement **automatic scheduled reports**.

Would you like me to implement **email-based report delivery** next? 🚀
---
### **Implementing Email-Based Report Delivery for Geofence History**
To allow users to **receive geofence reports via email**, we will:
1. **Modify the backend to generate and send email reports**.
2. **Add an API endpoint for email delivery**.
3. **Create a UI in React Native to request email reports**.

---

## **1. Modify Backend to Send Emails**
### **1.1. Add Dependencies for Email (JavaMail) in `pom.xml`**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

### **1.2. Configure Email in `application.properties`**
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-email-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```
> **Note:** Use **App Passwords** if using **Gmail** for SMTP.

---

### **1.3. Create Email Service**
Modify `EmailService.java` to send email reports.

```java
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final GeofenceExportService exportService;

    public EmailService(JavaMailSender mailSender, GeofenceExportService exportService) {
        this.mailSender = mailSender;
        this.exportService = exportService;
    }

    public void sendGeofenceReport(Long vehicleId, String email, String format) throws MessagingException {
        byte[] fileData;
        String filename;
        String contentType;

        if (format.equalsIgnoreCase("csv")) {
            fileData = exportService.generateCSV(vehicleId).getBytes();
            filename = "geofence_history.csv";
            contentType = "text/csv";
        } else {
            fileData = exportService.generatePDF(vehicleId);
            filename = "geofence_history.pdf";
            contentType = "application/pdf";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject("Geofence Alert History Report");
        helper.setText("Please find the attached geofence history report.");

        helper.addAttachment(filename, new ByteArrayDataSource(fileData, contentType));
        mailSender.send(message);
    }
}
```

---

### **1.4. Create API Endpoint for Sending Emails**
Modify `GeofenceController.java`:

```java
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/geofences")
public class GeofenceController {

    private final EmailService emailService;

    public GeofenceController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/{vehicleId}/send-report")
    public ResponseEntity<String> sendGeofenceReport(
            @PathVariable Long vehicleId,
            @RequestParam String email,
            @RequestParam String format) {

        try {
            emailService.sendGeofenceReport(vehicleId, email, format);
            return ResponseEntity.ok("Geofence report sent successfully to " + email);
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Failed to send email: " + e.getMessage());
        }
    }
}
```

---

## **2. Update API Service in React Native**
Modify `src/services/api.js` to send email requests.

```javascript
import axios from 'axios';
import { Alert } from 'react-native';

const API_BASE_URL = 'http://localhost:8080/api/geofences';

export const sendEmailReport = async (vehicleId, email, format) => {
  try {
    const response = await axios.post(
      `${API_BASE_URL}/${vehicleId}/send-report?email=${email}&format=${format}`
    );
    Alert.alert('Success', `Report sent to ${email}`);
  } catch (error) {
    console.error('Error sending email:', error);
    Alert.alert('Error', 'Failed to send email.');
  }
};
```

---

## **3. Modify UI to Request Email Reports**
Modify `src/screens/GeofenceHistoryScreen.js`:

```javascript
import React, { useState } from 'react';
import { View, Text, FlatList, Button, TextInput, StyleSheet, ActivityIndicator } from 'react-native';
import { getGeofenceHistory, sendEmailReport } from '../services/api';

const GeofenceHistoryScreen = ({ route }) => {
  const { vehicleId } = route.params;
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(true);
  const [email, setEmail] = useState('');

  useEffect(() => {
    fetchHistory();
  }, []);

  const fetchHistory = async () => {
    const data = await getGeofenceHistory(vehicleId);
    setHistory(data);
    setLoading(false);
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Geofence Alert History</Text>

      <TextInput 
        style={styles.input}
        placeholder="Enter email for report"
        value={email}
        onChangeText={setEmail}
      />

      <Button title="Send CSV Report" onPress={() => sendEmailReport(vehicleId, email, 'csv')} />
      <Button title="Send PDF Report" onPress={() => sendEmailReport(vehicleId, email, 'pdf')} />

      {loading ? (
        <ActivityIndicator size="large" color="#0000ff" />
      ) : (
        <FlatList
          data={history}
          keyExtractor={(item) => item.id.toString()}
          renderItem={({ item }) => (
            <View style={styles.alertItem}>
              <Text style={styles.event}>{item.event} {item.geofence.name}</Text>
              <Text style={styles.timestamp}>{new Date(item.timestamp).toLocaleString()}</Text>
            </View>
          )}
        />
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, padding: 20 },
  title: { fontSize: 24, fontWeight: 'bold', textAlign: 'center', marginBottom: 20 },
  input: { borderWidth: 1, padding: 10, marginBottom: 10 },
  alertItem: { padding: 10, borderBottomWidth: 1 },
  event: { fontSize: 18, fontWeight: 'bold' },
  timestamp: { color: 'gray' }
});

export default GeofenceHistoryScreen;
```

---

## **4. Update Navigation**
Modify `App.js`:

```javascript
import GeofenceHistoryScreen from './src/screens/GeofenceHistoryScreen';

<Stack.Screen name="Geofence History" component={GeofenceHistoryScreen} />
```

---

## **5. Run and Test**
1. **Start Spring Boot Backend**
   ```sh
   mvn spring-boot:run
   ```
2. **Start React Native App**
   ```sh
   npx react-native run-android
   ```
   OR
   ```sh
   npx react-native run-ios
   ```
3. **Test Email Report**
   - Enter an email address.
   - Click **Send CSV Report** or **Send PDF Report**.
   - Check the inbox for the report.

---

## **6. Expected Behavior**
✅ Users can request **geofence history reports** via email.  
✅ Reports are **generated and attached** as CSV or PDF.  
✅ Email is **sent successfully** with the requested file.  

---

## **7. Next Steps**
- Implement **scheduled reports** (e.g., daily/weekly).
- Add **report customization** (filter by date, geofence, etc.).

Would you like me to implement **automated scheduled reports** next? 🚀
---
### **Implementing Automated Scheduled Reports for Geofence History**
To enable **automated scheduled reports**, we will:
1. **Allow users to schedule daily/weekly email reports**.
2. **Modify the backend to handle scheduled reports**.
3. **Create a UI in React Native to manage scheduling**.
4. **Use Spring Boot’s `@Scheduled` annotation** to automate report generation.

---

## **1. Modify Backend for Scheduled Reports**
### **1.1. Create `ScheduledReport` Entity**
Modify `ScheduledReport.java` to store report preferences.

```java
import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
public class ScheduledReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long vehicleId;
    private String email;
    private String format; // "csv" or "pdf"
    private String frequency; // "daily" or "weekly"
    private LocalTime scheduledTime;

    public ScheduledReport() {}

    public ScheduledReport(Long vehicleId, String email, String format, String frequency, LocalTime scheduledTime) {
        this.vehicleId = vehicleId;
        this.email = email;
        this.format = format;
        this.frequency = frequency;
        this.scheduledTime = scheduledTime;
    }

    // Getters and Setters
}
```

---

### **1.2. Create `ScheduledReportRepository.java`**
```java
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScheduledReportRepository extends JpaRepository<ScheduledReport, Long> {
    List<ScheduledReport> findAll();
}
```

---

### **1.3. Implement `ScheduledReportService.java`**
Modify `ScheduledReportService.java` to schedule and send reports.

```java
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import java.time.LocalTime;
import java.util.List;

@Service
public class ScheduledReportService {

    private final ScheduledReportRepository reportRepository;
    private final EmailService emailService;

    public ScheduledReportService(ScheduledReportRepository reportRepository, EmailService emailService) {
        this.reportRepository = reportRepository;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 * * * ?") // Runs every hour
    public void sendScheduledReports() {
        List<ScheduledReport> reports = reportRepository.findAll();
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);

        for (ScheduledReport report : reports) {
            if (report.getScheduledTime().equals(now)) {
                try {
                    emailService.sendGeofenceReport(report.getVehicleId(), report.getEmail(), report.getFormat());
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ScheduledReport scheduleReport(ScheduledReport report) {
        return reportRepository.save(report);
    }

    public void deleteScheduledReport(Long id) {
        reportRepository.deleteById(id);
    }
}
```
> **Note:** The scheduler runs **every hour** and sends reports if the current time matches a stored scheduled time.

---

### **1.4. Create API Endpoints for Scheduling Reports**
Modify `ScheduledReportController.java`:

```java
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ScheduledReportController {

    private final ScheduledReportService reportService;

    public ScheduledReportController(ScheduledReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/schedule")
    public ScheduledReport scheduleReport(@RequestBody ScheduledReport report) {
        return reportService.scheduleReport(report);
    }

    @GetMapping
    public List<ScheduledReport> getScheduledReports() {
        return reportService.reportRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public void deleteScheduledReport(@PathVariable Long id) {
        reportService.deleteScheduledReport(id);
    }
}
```

---

## **2. Update API Service in React Native**
Modify `src/services/api.js` to schedule reports.

```javascript
import axios from 'axios';
import { Alert } from 'react-native';

const API_BASE_URL = 'http://localhost:8080/api/reports';

export const scheduleReport = async (vehicleId, email, format, frequency, scheduledTime) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/schedule`, {
      vehicleId,
      email,
      format,
      frequency,
      scheduledTime
    });
    Alert.alert('Success', 'Report scheduled successfully.');
  } catch (error) {
    console.error('Error scheduling report:', error);
    Alert.alert('Error', 'Failed to schedule report.');
  }
};

export const getScheduledReports = async () => {
  try {
    const response = await axios.get(API_BASE_URL);
    return response.data;
  } catch (error) {
    console.error('Error fetching scheduled reports:', error);
    return [];
  }
};

export const deleteScheduledReport = async (id) => {
  try {
    await axios.delete(`${API_BASE_URL}/${id}`);
    Alert.alert('Success', 'Scheduled report deleted.');
  } catch (error) {
    console.error('Error deleting scheduled report:', error);
    Alert.alert('Error', 'Failed to delete scheduled report.');
  }
};
```

---

## **3. Create UI for Managing Scheduled Reports**
Create `src/screens/ScheduledReportsScreen.js`:

```javascript
import React, { useEffect, useState } from 'react';
import { View, Text, FlatList, TextInput, Button, StyleSheet } from 'react-native';
import { scheduleReport, getScheduledReports, deleteScheduledReport } from '../services/api';

const ScheduledReportsScreen = () => {
  const [scheduledReports, setScheduledReports] = useState([]);
  const [email, setEmail] = useState('');
  const [format, setFormat] = useState('csv');
  const [frequency, setFrequency] = useState('daily');
  const [scheduledTime, setScheduledTime] = useState('');

  useEffect(() => {
    fetchScheduledReports();
  }, []);

  const fetchScheduledReports = async () => {
    const data = await getScheduledReports();
    setScheduledReports(data);
  };

  const handleScheduleReport = async () => {
    if (!email || !scheduledTime) {
      alert('Please enter email and time');
      return;
    }

    await scheduleReport(1, email, format, frequency, scheduledTime);
    fetchScheduledReports();
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Scheduled Reports</Text>

      <TextInput style={styles.input} placeholder="Email" onChangeText={setEmail} />
      <TextInput style={styles.input} placeholder="Time (HH:MM)" onChangeText={setScheduledTime} />

      <Button title="Schedule Daily CSV Report" onPress={() => handleScheduleReport()} />
      <Button title="Schedule Weekly PDF Report" onPress={() => handleScheduleReport()} />

      <FlatList
        data={scheduledReports}
        keyExtractor={(item) => item.id.toString()}
        renderItem={({ item }) => (
          <View style={styles.reportItem}>
            <Text>{item.email} - {item.format.toUpperCase()} - {item.frequency} at {item.scheduledTime}</Text>
            <Button title="Delete" onPress={() => deleteScheduledReport(item.id)} />
          </View>
        )}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, padding: 20 },
  title: { fontSize: 24, fontWeight: 'bold', textAlign: 'center', marginBottom: 20 },
  input: { borderWidth: 1, padding: 10, marginBottom: 10 },
  reportItem: { padding: 10, borderBottomWidth: 1 }
});

export default ScheduledReportsScreen;
```

---

## **4. Update Navigation**
Modify `App.js`:

```javascript
import ScheduledReportsScreen from './src/screens/ScheduledReportsScreen';

<Stack.Screen name="Scheduled Reports" component={ScheduledReportsScreen} />
```

---

## **5. Run and Test**
1. **Start Spring Boot Backend**
   ```sh
   mvn spring-boot:run
   ```
2. **Start React Native App**
   ```sh
   npx react-native run-android
   ```
   OR
   ```sh
   npx react-native run-ios
   ```
3. **Test Report Scheduling**
   - Enter an **email and time**.
   - Click **Schedule Report**.
   - Verify **scheduled reports list updates**.
   - Check the **email inbox at the scheduled time**.

---

## **6. Next Steps**
- Add **timezone support** for scheduling.
- Implement **report customization filters**.

Would you like me to implement **report filtering options** next? 🚀
---
### **Implementing Report Filtering Options for Geofence History**
To allow users to **customize geofence reports**, we will:
1. **Add filters for date range, geofence, and event type**.
2. **Modify the backend to handle filtered reports**.
3. **Update the UI in React Native for filtering options**.
4. **Ensure email reports can use filters**.

---

## **1. Modify Backend to Support Filtering**
### **1.1. Update Repository to Support Query Filters**
Modify `GeofenceAlertHistoryRepository.java`:

```java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface GeofenceAlertHistoryRepository extends JpaRepository<GeofenceAlertHistory, Long> {

    @Query("SELECT g FROM GeofenceAlertHistory g WHERE g.vehicle.id = :vehicleId " +
           "AND (:startDate IS NULL OR g.timestamp >= :startDate) " +
           "AND (:endDate IS NULL OR g.timestamp <= :endDate) " +
           "AND (:geofenceId IS NULL OR g.geofence.id = :geofenceId) " +
           "AND (:eventType IS NULL OR g.event = :eventType) " +
           "ORDER BY g.timestamp DESC")
    List<GeofenceAlertHistory> findFilteredHistory(
            @Param("vehicleId") Long vehicleId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("geofenceId") Long geofenceId,
            @Param("eventType") String eventType);
}
```

---

### **1.2. Modify Controller to Handle Filtered Requests**
Modify `GeofenceController.java`:

```java
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/geofences")
public class GeofenceController {

    private final GeofenceAlertHistoryRepository historyRepository;
    private final GeofenceExportService exportService;
    private final EmailService emailService;

    public GeofenceController(GeofenceAlertHistoryRepository historyRepository, 
                              GeofenceExportService exportService, 
                              EmailService emailService) {
        this.historyRepository = historyRepository;
        this.exportService = exportService;
        this.emailService = emailService;
    }

    @GetMapping("/{vehicleId}/history")
    public List<GeofenceAlertHistory> getFilteredHistory(
            @PathVariable Long vehicleId,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) Long geofenceId,
            @RequestParam(required = false) String eventType) {

        return historyRepository.findFilteredHistory(vehicleId, startDate, endDate, geofenceId, eventType);
    }

    @PostMapping("/{vehicleId}/send-filtered-report")
    public String sendFilteredReport(
            @PathVariable Long vehicleId,
            @RequestParam String email,
            @RequestParam String format,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) Long geofenceId,
            @RequestParam(required = false) String eventType) {

        byte[] fileData;
        if (format.equalsIgnoreCase("csv")) {
            fileData = exportService.generateFilteredCSV(vehicleId, startDate, endDate, geofenceId, eventType).getBytes();
        } else {
            fileData = exportService.generateFilteredPDF(vehicleId, startDate, endDate, geofenceId, eventType);
        }

        try {
            emailService.sendReportWithFilters(email, format, fileData);
            return "Filtered report sent successfully!";
        } catch (Exception e) {
            return "Failed to send email: " + e.getMessage();
        }
    }
}
```

---

## **2. Update API Service in React Native**
Modify `src/services/api.js` to support filtering.

```javascript
import axios from 'axios';
import { Alert } from 'react-native';

const API_BASE_URL = 'http://localhost:8080/api/geofences';

export const getFilteredGeofenceHistory = async (vehicleId, filters) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${vehicleId}/history`, { params: filters });
    return response.data;
  } catch (error) {
    console.error('Error fetching filtered history:', error);
    return [];
  }
};

export const sendFilteredEmailReport = async (vehicleId, email, format, filters) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/${vehicleId}/send-filtered-report`, null, { 
      params: { email, format, ...filters }
    });
    Alert.alert('Success', 'Filtered report sent to ' + email);
  } catch (error) {
    console.error('Error sending filtered email:', error);
    Alert.alert('Error', 'Failed to send filtered report.');
  }
};
```

---

## **3. Modify UI for Filtering Reports**
Modify `src/screens/GeofenceHistoryScreen.js`:

```javascript
import React, { useState } from 'react';
import { View, Text, FlatList, Button, TextInput, Picker, StyleSheet, ActivityIndicator } from 'react-native';
import { getFilteredGeofenceHistory, sendFilteredEmailReport } from '../services/api';

const GeofenceHistoryScreen = ({ route }) => {
  const { vehicleId } = route.params;
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(true);
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [geofenceId, setGeofenceId] = useState('');
  const [eventType, setEventType] = useState('');
  const [email, setEmail] = useState('');
  const [format, setFormat] = useState('csv');

  const fetchFilteredHistory = async () => {
    setLoading(true);
    const filters = { startDate, endDate, geofenceId, eventType };
    const data = await getFilteredGeofenceHistory(vehicleId, filters);
    setHistory(data);
    setLoading(false);
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Geofence Alert History</Text>

      <TextInput style={styles.input} placeholder="Start Date (YYYY-MM-DD)" onChangeText={setStartDate} />
      <TextInput style={styles.input} placeholder="End Date (YYYY-MM-DD)" onChangeText={setEndDate} />
      <TextInput style={styles.input} placeholder="Geofence ID (optional)" onChangeText={setGeofenceId} />
      
      <Picker selectedValue={eventType} onValueChange={(value) => setEventType(value)}>
        <Picker.Item label="All Events" value="" />
        <Picker.Item label="Entry" value="ENTERED" />
        <Picker.Item label="Exit" value="EXITED" />
      </Picker>

      <Button title="Apply Filters" onPress={fetchFilteredHistory} />

      <TextInput style={styles.input} placeholder="Email for report" onChangeText={setEmail} />
      <Picker selectedValue={format} onValueChange={(value) => setFormat(value)}>
        <Picker.Item label="CSV" value="csv" />
        <Picker.Item label="PDF" value="pdf" />
      </Picker>
      <Button title="Send Filtered Report" onPress={() => sendFilteredEmailReport(vehicleId, email, format, { startDate, endDate, geofenceId, eventType })} />

      {loading ? (
        <ActivityIndicator size="large" color="#0000ff" />
      ) : (
        <FlatList
          data={history}
          keyExtractor={(item) => item.id.toString()}
          renderItem={({ item }) => (
            <View style={styles.alertItem}>
              <Text style={styles.event}>{item.event} {item.geofence.name}</Text>
              <Text style={styles.timestamp}>{new Date(item.timestamp).toLocaleString()}</Text>
            </View>
          )}
        />
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, padding: 20 },
  title: { fontSize: 24, fontWeight: 'bold', textAlign: 'center', marginBottom: 20 },
  input: { borderWidth: 1, padding: 10, marginBottom: 10 },
  alertItem: { padding: 10, borderBottomWidth: 1 },
  event: { fontSize: 18, fontWeight: 'bold' },
  timestamp: { color: 'gray' }
});

export default GeofenceHistoryScreen;
```

---

## **Next Steps**
- Implement **advanced filtering (e.g., vehicle type, location radius)**.
- Add **push notifications based on filtered alerts**.

Would you like me to implement **push notifications for specific filtered events** next? 🚀
---
### **Implementing Push Notifications for Filtered Geofence Events**
To allow users to receive **push notifications based on specific geofence events**, we will:
1. **Let users define notification preferences for geofence events**.
2. **Modify the backend to handle filtered notifications**.
3. **Update React Native to register for specific notifications**.
4. **Ensure push notifications are sent based on user-defined filters**.

---

## **1. Modify Backend to Store Notification Preferences**
### **1.1. Create `GeofenceNotificationPreference` Entity**
Modify `GeofenceNotificationPreference.java` to store user-defined filters for notifications.

```java
import jakarta.persistence.*;

@Entity
public class GeofenceNotificationPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;
    private Long vehicleId;
    private Long geofenceId; // Optional: filter by specific geofence
    private String eventType; // "ENTERED", "EXITED", or "BOTH"
    private boolean active;

    public GeofenceNotificationPreference() {}

    public GeofenceNotificationPreference(String userEmail, Long vehicleId, Long geofenceId, String eventType, boolean active) {
        this.userEmail = userEmail;
        this.vehicleId = vehicleId;
        this.geofenceId = geofenceId;
        this.eventType = eventType;
        this.active = active;
    }

    // Getters and Setters
}
```

---

### **1.2. Create `GeofenceNotificationPreferenceRepository`**
```java
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GeofenceNotificationPreferenceRepository extends JpaRepository<GeofenceNotificationPreference, Long> {
    List<GeofenceNotificationPreference> findByVehicleIdAndActiveTrue(Long vehicleId);
}
```

---

### **1.3. Modify `GeofenceService` to Send Filtered Notifications**
Modify `GeofenceService.java` to check user preferences before sending notifications.

```java
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GeofenceService {

    private final GeofenceNotificationPreferenceRepository preferenceRepository;
    private final GeofenceRepository geofenceRepository;
    private final VehicleRepository vehicleRepository;
    private final FirebaseMessagingService firebaseMessagingService;

    public GeofenceService(GeofenceNotificationPreferenceRepository preferenceRepository, 
                           GeofenceRepository geofenceRepository, 
                           VehicleRepository vehicleRepository,
                           FirebaseMessagingService firebaseMessagingService) {
        this.preferenceRepository = preferenceRepository;
        this.geofenceRepository = geofenceRepository;
        this.vehicleRepository = vehicleRepository;
        this.firebaseMessagingService = firebaseMessagingService;
    }

    public void checkGeofence(Long vehicleId, double latitude, double longitude, String fcmToken) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElse(null);
        if (vehicle == null) return;

        List<GeofenceNotificationPreference> preferences = preferenceRepository.findByVehicleIdAndActiveTrue(vehicleId);

        for (Geofence geofence : geofenceRepository.findAll()) {
            double distance = calculateDistance(latitude, longitude, geofence.getLatitude(), geofence.getLongitude());
            boolean isInside = distance <= geofence.getRadius();

            for (GeofenceNotificationPreference preference : preferences) {
                if (preference.getGeofenceId() != null && !preference.getGeofenceId().equals(geofence.getId())) {
                    continue; // Skip if preference is for another geofence
                }

                if (isInside && !vehicle.isInsideGeofence() && ("ENTERED".equals(preference.getEventType()) || "BOTH".equals(preference.getEventType()))) {
                    vehicle.setInsideGeofence(true);
                    sendFilteredNotification(preference.getUserEmail(), "Vehicle ENTERED " + geofence.getName());
                } 
                else if (!isInside && vehicle.isInsideGeofence() && ("EXITED".equals(preference.getEventType()) || "BOTH".equals(preference.getEventType()))) {
                    vehicle.setInsideGeofence(false);
                    sendFilteredNotification(preference.getUserEmail(), "Vehicle EXITED " + geofence.getName());
                }
            }

            vehicleRepository.save(vehicle);
        }
    }

    private void sendFilteredNotification(String email, String message) {
        try {
            firebaseMessagingService.sendPushNotification(email, "Geofence Alert", message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}
```

---

### **1.4. Create API Endpoints for Notification Preferences**
Modify `GeofenceNotificationPreferenceController.java`:

```java
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class GeofenceNotificationPreferenceController {

    private final GeofenceNotificationPreferenceRepository preferenceRepository;

    public GeofenceNotificationPreferenceController(GeofenceNotificationPreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
    }

    @GetMapping("/{userEmail}")
    public List<GeofenceNotificationPreference> getUserPreferences(@PathVariable String userEmail) {
        return preferenceRepository.findAll();
    }

    @PostMapping("/subscribe")
    public GeofenceNotificationPreference subscribeToNotifications(@RequestBody GeofenceNotificationPreference preference) {
        return preferenceRepository.save(preference);
    }

    @DeleteMapping("/{id}")
    public void unsubscribeFromNotifications(@PathVariable Long id) {
        preferenceRepository.deleteById(id);
    }
}
```

---

## **2. Update API Service in React Native**
Modify `src/services/api.js`:

```javascript
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/notifications';

export const subscribeToGeofenceNotifications = async (email, vehicleId, geofenceId, eventType) => {
  try {
    await axios.post(`${API_BASE_URL}/subscribe`, { email, vehicleId, geofenceId, eventType, active: true });
    alert('Subscribed to notifications.');
  } catch (error) {
    console.error('Error subscribing to notifications:', error);
  }
};

export const getGeofenceNotificationPreferences = async (userEmail) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${userEmail}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching notification preferences:', error);
    return [];
  }
};

export const unsubscribeFromGeofenceNotifications = async (id) => {
  try {
    await axios.delete(`${API_BASE_URL}/${id}`);
    alert('Unsubscribed from notifications.');
  } catch (error) {
    console.error('Error unsubscribing:', error);
  }
};
```

---

## **3. Create UI for Notification Subscriptions**
Create `src/screens/GeofenceNotificationScreen.js`:

```javascript
import React, { useEffect, useState } from 'react';
import { View, Text, Picker, Button, TextInput, FlatList, StyleSheet } from 'react-native';
import { subscribeToGeofenceNotifications, getGeofenceNotificationPreferences, unsubscribeFromGeofenceNotifications } from '../services/api';

const GeofenceNotificationScreen = () => {
  const [email, setEmail] = useState('');
  const [vehicleId, setVehicleId] = useState('');
  const [geofenceId, setGeofenceId] = useState('');
  const [eventType, setEventType] = useState('BOTH');
  const [subscriptions, setSubscriptions] = useState([]);

  useEffect(() => {
    fetchSubscriptions();
  }, []);

  const fetchSubscriptions = async () => {
    if (email) {
      const data = await getGeofenceNotificationPreferences(email);
      setSubscriptions(data);
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Geofence Notification Preferences</Text>
      
      <TextInput style={styles.input} placeholder="Your Email" onChangeText={setEmail} />
      <TextInput style={styles.input} placeholder="Vehicle ID" onChangeText={setVehicleId} />
      <TextInput style={styles.input} placeholder="Geofence ID (optional)" onChangeText={setGeofenceId} />
      
      <Picker selectedValue={eventType} onValueChange={(value) => setEventType(value)}>
        <Picker.Item label="Both Entry & Exit" value="BOTH" />
        <Picker.Item label="Only Entry" value="ENTERED" />
        <Picker.Item label="Only Exit" value="EXITED" />
      </Picker>

      <Button title="Subscribe" onPress={() => subscribeToGeofenceNotifications(email, vehicleId, geofenceId, eventType)} />
    </View>
  );
};

export default GeofenceNotificationScreen;
```

---

## **Next Steps**
- Add **real-time WebSocket-based push notifications**.
- Implement **geofence-specific settings for alerts**.

Would you like me to implement **WebSocket push notifications for real-time geofence events** next? 🚀
---
### **Implementing WebSocket-Based Push Notifications for Real-Time Geofence Events**
To enable **real-time geofence alerts** via **WebSockets**, we will:
1. **Modify the backend to send geofence event notifications via WebSockets**.
2. **Update the React Native app to listen for WebSocket events**.
3. **Ensure push notifications and WebSocket messages work together**.

---

## **1. Modify Backend to Send WebSocket Notifications**
### **1.1. Add WebSocket Configuration**
Modify `WebSocketConfig.java`:

```java
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*");
    }
}
```
> This sets up a WebSocket endpoint at `/ws` and allows real-time messages to be published to `/topic/geofence-alerts`.

---

### **1.2. Modify `GeofenceService` to Broadcast Alerts**
Modify `GeofenceService.java`:

```java
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GeofenceService {

    private final SimpMessagingTemplate messagingTemplate;
    private final GeofenceRepository geofenceRepository;
    private final VehicleRepository vehicleRepository;
    private final GeofenceNotificationPreferenceRepository preferenceRepository;

    public GeofenceService(SimpMessagingTemplate messagingTemplate, 
                           GeofenceRepository geofenceRepository, 
                           VehicleRepository vehicleRepository, 
                           GeofenceNotificationPreferenceRepository preferenceRepository) {
        this.messagingTemplate = messagingTemplate;
        this.geofenceRepository = geofenceRepository;
        this.vehicleRepository = vehicleRepository;
        this.preferenceRepository = preferenceRepository;
    }

    public void checkGeofence(Long vehicleId, double latitude, double longitude) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElse(null);
        if (vehicle == null) return;

        List<GeofenceNotificationPreference> preferences = preferenceRepository.findByVehicleIdAndActiveTrue(vehicleId);

        for (Geofence geofence : geofenceRepository.findAll()) {
            double distance = calculateDistance(latitude, longitude, geofence.getLatitude(), geofence.getLongitude());
            boolean isInside = distance <= geofence.getRadius();

            for (GeofenceNotificationPreference preference : preferences) {
                if (preference.getGeofenceId() != null && !preference.getGeofenceId().equals(geofence.getId())) {
                    continue;
                }

                if (isInside && !vehicle.isInsideGeofence() && ("ENTERED".equals(preference.getEventType()) || "BOTH".equals(preference.getEventType()))) {
                    vehicle.setInsideGeofence(true);
                    sendGeofenceNotification(vehicleId, geofence.getName(), "ENTERED");
                } else if (!isInside && vehicle.isInsideGeofence() && ("EXITED".equals(preference.getEventType()) || "BOTH".equals(preference.getEventType()))) {
                    vehicle.setInsideGeofence(false);
                    sendGeofenceNotification(vehicleId, geofence.getName(), "EXITED");
                }
            }

            vehicleRepository.save(vehicle);
        }
    }

    private void sendGeofenceNotification(Long vehicleId, String geofenceName, String event) {
        String message = "Vehicle " + vehicleId + " " + event + " geofence: " + geofenceName;
        messagingTemplate.convertAndSend("/topic/geofence-alerts", message);
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}
```
> This method **publishes real-time messages** to `/topic/geofence-alerts` whenever a vehicle enters or exits a geofence.

---

## **2. Modify React Native to Listen for WebSocket Messages**
### **2.1. Install WebSocket Client**
```sh
npm install @stomp/stompjs
```

### **2.2. Modify WebSocket Service**
Modify `src/services/websocket.js`:

```javascript
import { Client } from '@stomp/stompjs';

const socketURL = 'ws://localhost:8080/ws';

export const socketClient = new Client({
  brokerURL: socketURL,
  debug: (str) => console.log(str),
  onConnect: () => console.log('Connected to WebSocket'),
  onDisconnect: () => console.log('Disconnected from WebSocket'),
});

export const subscribeToGeofenceAlerts = (callback) => {
  socketClient.activate();

  socketClient.onConnect = () => {
    socketClient.subscribe('/topic/geofence-alerts', (message) => {
      callback(message.body);
    });
  };
};
```
> This **listens for messages** from `/topic/geofence-alerts` and triggers a callback when an alert is received.

---

## **3. Modify React Native to Display Real-Time Alerts**
Modify `src/screens/HomeScreen.js`:

```javascript
import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, FlatList } from 'react-native';
import { subscribeToGeofenceAlerts } from '../services/websocket';

const HomeScreen = () => {
  const [alerts, setAlerts] = useState([]);

  useEffect(() => {
    subscribeToGeofenceAlerts((message) => {
      setAlerts((prevAlerts) => [message, ...prevAlerts]);
    });
  }, []);

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Geofence Alerts</Text>
      <FlatList
        data={alerts}
        keyExtractor={(item, index) => index.toString()}
        renderItem={({ item }) => <Text style={styles.alert}>{item}</Text>}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: 'center', alignItems: 'center' },
  title: { fontSize: 24, fontWeight: 'bold', marginBottom: 20 },
  alert: { fontSize: 18, color: 'red', marginVertical: 5 }
});

export default HomeScreen;
```
> This displays **real-time geofence alerts** as they are received from WebSockets.

---

## **4. Run and Test**
1. **Start Spring Boot Backend**
   ```sh
   mvn spring-boot:run
   ```
2. **Start React Native App**
   ```sh
   npx react-native run-android
   ```
   OR
   ```sh
   npx react-native run-ios
   ```
3. **Test WebSocket Alerts**
   - Move a vehicle **inside or outside** a geofenced area.
   - Check if the **WebSocket messages appear** in the app.
   - Verify **real-time updates without refreshing**.

---

## **5. Expected Behavior**
✅ **Real-time WebSocket messages** when a vehicle enters/exits a geofence.  
✅ **Geofence alerts are received instantly** without refreshing.  
✅ **User preferences filter out unwanted notifications**.

---

## **6. Next Steps**
- Implement **geofence alert sound and vibration** for WebSocket alerts.
- Optimize **battery efficiency for WebSocket listeners**.

Would you like me to implement **sound & vibration alerts for geofence notifications** next? 🚀
---
### **Implementing Sound & Vibration Alerts for Geofence Notifications**
To **enhance real-time geofence alerts**, we will:
1. **Enable sound and vibration notifications when an alert is received**.
2. **Modify the React Native app to handle notifications with sound & vibration**.
3. **Ensure settings allow users to enable/disable these features**.

---

## **1. Install Dependencies for Sound & Vibration**
```sh
npm install @react-native-community/push-notification react-native-sound react-native-vibrate
```
> Ensure you run `pod install` if using iOS:
```sh
cd ios && pod install && cd ..
```

---

## **2. Configure Push Notifications for Sound & Vibration**
Modify `src/services/notifications.js`:

```javascript
import PushNotification from '@react-native-community/push-notification';
import { Vibration } from 'react-native';
import Sound from 'react-native-sound';

const alertSound = new Sound('alert.mp3', Sound.MAIN_BUNDLE, (error) => {
  if (error) {
    console.error('Failed to load sound', error);
  }
});

export const triggerLocalNotification = (message) => {
  PushNotification.localNotification({
    title: 'Geofence Alert',
    message: message,
    playSound: true,
    soundName: 'default',
    vibrate: true,
    vibration: 500,
  });

  // Play alert sound
  alertSound.play();

  // Vibrate the device
  Vibration.vibrate(500);
};
```
> **Ensure an `alert.mp3` file is added to the `android/app/src/main/res/raw/` directory and `ios` Xcode project.**

---

## **3. Modify WebSocket Listener to Trigger Sound & Vibration**
Modify `src/services/websocket.js`:

```javascript
import { Client } from '@stomp/stompjs';
import { triggerLocalNotification } from './notifications';

const socketURL = 'ws://localhost:8080/ws';

export const socketClient = new Client({
  brokerURL: socketURL,
  debug: (str) => console.log(str),
  onConnect: () => console.log('Connected to WebSocket'),
  onDisconnect: () => console.log('Disconnected from WebSocket'),
});

export const subscribeToGeofenceAlerts = (callback) => {
  socketClient.activate();

  socketClient.onConnect = () => {
    socketClient.subscribe('/topic/geofence-alerts', (message) => {
      callback(message.body);
      triggerLocalNotification(message.body); // Trigger sound & vibration
    });
  };
};
```
> **Every WebSocket alert now triggers a sound and vibration notification.**

---

## **4. Modify React Native UI to Toggle Sound & Vibration**
Modify `src/screens/NotificationSettingsScreen.js`:

```javascript
import React, { useEffect, useState } from 'react';
import { View, Text, Switch, Button, StyleSheet } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';

const NotificationSettingsScreen = () => {
  const [soundEnabled, setSoundEnabled] = useState(true);
  const [vibrationEnabled, setVibrationEnabled] = useState(true);

  useEffect(() => {
    loadPreferences();
  }, []);

  const loadPreferences = async () => {
    const sound = await AsyncStorage.getItem('soundEnabled');
    const vibration = await AsyncStorage.getItem('vibrationEnabled');
    setSoundEnabled(sound !== 'false');
    setVibrationEnabled(vibration !== 'false');
  };

  const toggleSound = async () => {
    setSoundEnabled(!soundEnabled);
    await AsyncStorage.setItem('soundEnabled', String(!soundEnabled));
  };

  const toggleVibration = async () => {
    setVibrationEnabled(!vibrationEnabled);
    await AsyncStorage.setItem('vibrationEnabled', String(!vibrationEnabled));
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Notification Settings</Text>
      
      <View style={styles.setting}>
        <Text>Enable Sound</Text>
        <Switch value={soundEnabled} onValueChange={toggleSound} />
      </View>

      <View style={styles.setting}>
        <Text>Enable Vibration</Text>
        <Switch value={vibrationEnabled} onValueChange={toggleVibration} />
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: 'center', padding: 20 },
  title: { fontSize: 24, fontWeight: 'bold', textAlign: 'center', marginBottom: 20 },
  setting: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 20 }
});

export default NotificationSettingsScreen;
```
> This screen allows users to **enable/disable sound & vibration** for geofence alerts.

---

## **5. Modify Notification Trigger Based on Settings**
Modify `src/services/notifications.js`:

```javascript
export const triggerLocalNotification = async (message) => {
  const soundEnabled = await AsyncStorage.getItem('soundEnabled');
  const vibrationEnabled = await AsyncStorage.getItem('vibrationEnabled');

  PushNotification.localNotification({
    title: 'Geofence Alert',
    message: message,
    playSound: soundEnabled !== 'false',
    soundName: 'default',
    vibrate: vibrationEnabled !== 'false',
    vibration: vibrationEnabled !== 'false' ? 500 : 0,
  });

  if (soundEnabled !== 'false') {
    alertSound.play();
  }

  if (vibrationEnabled !== 'false') {
    Vibration.vibrate(500);
  }
};
```
> **Now, alerts respect user preferences for sound and vibration.**

---

## **6. Update Navigation**
Modify `App.js`:

```javascript
import NotificationSettingsScreen from './src/screens/NotificationSettingsScreen';

<Stack.Screen name="Notification Settings" component={NotificationSettingsScreen} />
```

---

## **7. Run and Test**
1. **Start Spring Boot Backend**
   ```sh
   mvn spring-boot:run
   ```
2. **Start React Native App**
   ```sh
   npx react-native run-android
   ```
   OR
   ```sh
   npx react-native run-ios
   ```
3. **Test Notifications**
   - Trigger a **geofence entry/exit**.
   - Verify **sound & vibration work**.
   - Disable sound/vibration in **settings** and test again.

---

## **8. Expected Behavior**
✅ **Geofence alerts trigger sound & vibration** by default.  
✅ **Users can enable/disable sound & vibration** via settings.  
✅ **Alerts appear instantly without refreshing**.

---

## **9. Next Steps**
- Implement **custom sound selection** for alerts.
- Optimize **battery efficiency for WebSocket connections**.

Would you like me to implement **custom alert sounds for different geofence events** next? 🚀
---
### **Implementing Custom Alert Sounds for Different Geofence Events**
To allow users to **set different sounds for entry and exit alerts**, we will:
1. **Add multiple sound files for different alert types**.
2. **Modify the React Native app to play specific sounds based on event type**.
3. **Update notification settings to allow users to choose custom sounds**.

---

## **1. Add Custom Sound Files**
1. **Prepare sound files** (`entry.mp3` and `exit.mp3`).
2. **Place them in the correct directories**:
   - **Android:** Move files to `android/app/src/main/res/raw/`.
   - **iOS:** Add them to the **Xcode project under `Resources`**.

---

## **2. Install Dependencies for Sound Playback**
If not installed already:
```sh
npm install react-native-sound
```
Run:
```sh
cd ios && pod install && cd ..
```

---

## **3. Modify Notification Service to Handle Custom Sounds**
Modify `src/services/notifications.js`:

```javascript
import { Vibration } from 'react-native';
import Sound from 'react-native-sound';
import AsyncStorage from '@react-native-async-storage/async-storage';

const entrySound = new Sound('entry.mp3', Sound.MAIN_BUNDLE, (error) => {
  if (error) console.error('Failed to load entry sound', error);
});

const exitSound = new Sound('exit.mp3', Sound.MAIN_BUNDLE, (error) => {
  if (error) console.error('Failed to load exit sound', error);
});

export const triggerLocalNotification = async (message, eventType) => {
  const soundEnabled = await AsyncStorage.getItem('soundEnabled');
  const vibrationEnabled = await AsyncStorage.getItem('vibrationEnabled');
  const entrySoundEnabled = await AsyncStorage.getItem('entrySoundEnabled');
  const exitSoundEnabled = await AsyncStorage.getItem('exitSoundEnabled');

  PushNotification.localNotification({
    title: 'Geofence Alert',
    message: message,
    playSound: soundEnabled !== 'false',
    soundName: 'default',
    vibrate: vibrationEnabled !== 'false',
    vibration: vibrationEnabled !== 'false' ? 500 : 0,
  });

  // Play sound based on event type
  if (soundEnabled !== 'false') {
    if (eventType === 'ENTERED' && entrySoundEnabled !== 'false') {
      entrySound.play();
    } else if (eventType === 'EXITED' && exitSoundEnabled !== 'false') {
      exitSound.play();
    }
  }

  // Vibrate if enabled
  if (vibrationEnabled !== 'false') {
    Vibration.vibrate(500);
  }
};
```
> **Now, geofence entry alerts play `entry.mp3` and exit alerts play `exit.mp3`**.

---

## **4. Modify WebSocket Listener to Include Event Type**
Modify `src/services/websocket.js`:

```javascript
import { Client } from '@stomp/stompjs';
import { triggerLocalNotification } from './notifications';

const socketURL = 'ws://localhost:8080/ws';

export const socketClient = new Client({
  brokerURL: socketURL,
  debug: (str) => console.log(str),
  onConnect: () => console.log('Connected to WebSocket'),
  onDisconnect: () => console.log('Disconnected from WebSocket'),
});

export const subscribeToGeofenceAlerts = (callback) => {
  socketClient.activate();

  socketClient.onConnect = () => {
    socketClient.subscribe('/topic/geofence-alerts', (message) => {
      callback(message.body);

      // Extract event type from message (assume format "Vehicle X ENTERED geofence Y")
      const eventType = message.body.includes('ENTERED') ? 'ENTERED' : 'EXITED';
      triggerLocalNotification(message.body, eventType);
    });
  };
};
```
> **This ensures the correct sound is played for entry/exit events.**

---

## **5. Modify Notification Settings to Choose Custom Sounds**
Modify `src/screens/NotificationSettingsScreen.js`:

```javascript
import React, { useEffect, useState } from 'react';
import { View, Text, Switch, Button, StyleSheet } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';

const NotificationSettingsScreen = () => {
  const [soundEnabled, setSoundEnabled] = useState(true);
  const [vibrationEnabled, setVibrationEnabled] = useState(true);
  const [entrySoundEnabled, setEntrySoundEnabled] = useState(true);
  const [exitSoundEnabled, setExitSoundEnabled] = useState(true);

  useEffect(() => {
    loadPreferences();
  }, []);

  const loadPreferences = async () => {
    const sound = await AsyncStorage.getItem('soundEnabled');
    const vibration = await AsyncStorage.getItem('vibrationEnabled');
    const entrySound = await AsyncStorage.getItem('entrySoundEnabled');
    const exitSound = await AsyncStorage.getItem('exitSoundEnabled');

    setSoundEnabled(sound !== 'false');
    setVibrationEnabled(vibration !== 'false');
    setEntrySoundEnabled(entrySound !== 'false');
    setExitSoundEnabled(exitSound !== 'false');
  };

  const toggleSound = async () => {
    setSoundEnabled(!soundEnabled);
    await AsyncStorage.setItem('soundEnabled', String(!soundEnabled));
  };

  const toggleVibration = async () => {
    setVibrationEnabled(!vibrationEnabled);
    await AsyncStorage.setItem('vibrationEnabled', String(!vibrationEnabled));
  };

  const toggleEntrySound = async () => {
    setEntrySoundEnabled(!entrySoundEnabled);
    await AsyncStorage.setItem('entrySoundEnabled', String(!entrySoundEnabled));
  };

  const toggleExitSound = async () => {
    setExitSoundEnabled(!exitSoundEnabled);
    await AsyncStorage.setItem('exitSoundEnabled', String(!exitSoundEnabled));
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Notification Settings</Text>

      <View style={styles.setting}>
        <Text>Enable Sound</Text>
        <Switch value={soundEnabled} onValueChange={toggleSound} />
      </View>

      <View style={styles.setting}>
        <Text>Enable Vibration</Text>
        <Switch value={vibrationEnabled} onValueChange={toggleVibration} />
      </View>

      <View style={styles.setting}>
        <Text>Enable Entry Alert Sound</Text>
        <Switch value={entrySoundEnabled} onValueChange={toggleEntrySound} />
      </View>

      <View style={styles.setting}>
        <Text>Enable Exit Alert Sound</Text>
        <Switch value={exitSoundEnabled} onValueChange={toggleExitSound} />
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: 'center', padding: 20 },
  title: { fontSize: 24, fontWeight: 'bold', textAlign: 'center', marginBottom: 20 },
  setting: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 20 }
});

export default NotificationSettingsScreen;
```
> **Users can now toggle sound settings for entry and exit alerts separately.**

---

## **6. Update Navigation**
Modify `App.js`:

```javascript
import NotificationSettingsScreen from './src/screens/NotificationSettingsScreen';

<Stack.Screen name="Notification Settings" component={NotificationSettingsScreen} />
```

---

## **7. Run and Test**
1. **Start Spring Boot Backend**
   ```sh
   mvn spring-boot:run
   ```
2. **Start React Native App**
   ```sh
   npx react-native run-android
   ```
   OR
   ```sh
   npx react-native run-ios
   ```
3. **Test Custom Alert Sounds**
   - Move a vehicle **inside or outside** a geofence.
   - Verify that **entry alerts play `entry.mp3`**.
   - Verify that **exit alerts play `exit.mp3`**.
   - Disable **entry/exit sounds** in settings and confirm they don't play.

---

## **8. Expected Behavior**
✅ **Different sounds play for entry and exit alerts**.  
✅ **Users can enable/disable entry/exit sounds separately**.  
✅ **Notifications appear instantly without app refresh**.  

---

## **9. Next Steps**
- Allow users to **upload their own custom sounds**.
- Optimize **battery usage for WebSocket listeners**.

Would you like me to implement **user-uploaded custom alert sounds** next? 🚀
---
### **Implementing User-Uploaded Custom Alert Sounds for Geofence Notifications**
To allow users to **upload their own custom sounds for geofence alerts**, we will:
1. **Enable users to select and upload custom sound files**.
2. **Modify the React Native app to store and use uploaded sounds**.
3. **Update settings to allow users to choose between default and custom sounds**.

---

## **1. Install Dependencies for File Selection**
We will use `react-native-document-picker` to allow users to select sound files.

```sh
npm install react-native-document-picker react-native-fs
```
For **iOS**, run:
```sh
cd ios && pod install && cd ..
```

---

## **2. Modify Notification Service to Play Custom Sounds**
Modify `src/services/notifications.js`:

```javascript
import { Vibration } from 'react-native';
import Sound from 'react-native-sound';
import AsyncStorage from '@react-native-async-storage/async-storage';
import RNFS from 'react-native-fs';

let entrySound;
let exitSound;

// Load custom sounds from storage if available
export const loadCustomSounds = async () => {
  const entrySoundPath = await AsyncStorage.getItem('entrySoundPath');
  const exitSoundPath = await AsyncStorage.getItem('exitSoundPath');

  entrySound = entrySoundPath
    ? new Sound(entrySoundPath, '', (error) => {
        if (error) console.error('Failed to load entry sound', error);
      })
    : new Sound('entry.mp3', Sound.MAIN_BUNDLE);

  exitSound = exitSoundPath
    ? new Sound(exitSoundPath, '', (error) => {
        if (error) console.error('Failed to load exit sound', error);
      })
    : new Sound('exit.mp3', Sound.MAIN_BUNDLE);
};

export const triggerLocalNotification = async (message, eventType) => {
  const soundEnabled = await AsyncStorage.getItem('soundEnabled');
  const vibrationEnabled = await AsyncStorage.getItem('vibrationEnabled');
  const entrySoundEnabled = await AsyncStorage.getItem('entrySoundEnabled');
  const exitSoundEnabled = await AsyncStorage.getItem('exitSoundEnabled');

  PushNotification.localNotification({
    title: 'Geofence Alert',
    message: message,
    playSound: soundEnabled !== 'false',
    soundName: 'default',
    vibrate: vibrationEnabled !== 'false',
    vibration: vibrationEnabled !== 'false' ? 500 : 0,
  });

  // Play sound based on event type
  if (soundEnabled !== 'false') {
    if (eventType === 'ENTERED' && entrySoundEnabled !== 'false' && entrySound) {
      entrySound.play();
    } else if (eventType === 'EXITED' && exitSoundEnabled !== 'false' && exitSound) {
      exitSound.play();
    }
  }

  // Vibrate if enabled
  if (vibrationEnabled !== 'false') {
    Vibration.vibrate(500);
  }
};
```
> **Now, the app checks if a custom sound exists and plays it instead of the default one.**

---

## **3. Modify Notification Settings to Upload Custom Sounds**
Modify `src/screens/NotificationSettingsScreen.js`:

```javascript
import React, { useEffect, useState } from 'react';
import { View, Text, Switch, Button, StyleSheet, Alert } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import DocumentPicker from 'react-native-document-picker';
import RNFS from 'react-native-fs';
import { loadCustomSounds } from '../services/notifications';

const NotificationSettingsScreen = () => {
  const [soundEnabled, setSoundEnabled] = useState(true);
  const [vibrationEnabled, setVibrationEnabled] = useState(true);
  const [entrySoundEnabled, setEntrySoundEnabled] = useState(true);
  const [exitSoundEnabled, setExitSoundEnabled] = useState(true);

  useEffect(() => {
    loadPreferences();
  }, []);

  const loadPreferences = async () => {
    const sound = await AsyncStorage.getItem('soundEnabled');
    const vibration = await AsyncStorage.getItem('vibrationEnabled');
    const entrySound = await AsyncStorage.getItem('entrySoundEnabled');
    const exitSound = await AsyncStorage.getItem('exitSoundEnabled');

    setSoundEnabled(sound !== 'false');
    setVibrationEnabled(vibration !== 'false');
    setEntrySoundEnabled(entrySound !== 'false');
    setExitSoundEnabled(exitSound !== 'false');
  };

  const toggleSound = async () => {
    setSoundEnabled(!soundEnabled);
    await AsyncStorage.setItem('soundEnabled', String(!soundEnabled));
  };

  const toggleVibration = async () => {
    setVibrationEnabled(!vibrationEnabled);
    await AsyncStorage.setItem('vibrationEnabled', String(!vibrationEnabled));
  };

  const toggleEntrySound = async () => {
    setEntrySoundEnabled(!entrySoundEnabled);
    await AsyncStorage.setItem('entrySoundEnabled', String(!entrySoundEnabled));
  };

  const toggleExitSound = async () => {
    setExitSoundEnabled(!exitSoundEnabled);
    await AsyncStorage.setItem('exitSoundEnabled', String(!exitSoundEnabled));
  };

  const selectSoundFile = async (type) => {
    try {
      const res = await DocumentPicker.pickSingle({
        type: DocumentPicker.types.audio,
      });

      const destinationPath = `${RNFS.DocumentDirectoryPath}/${res.name}`;
      await RNFS.copyFile(res.uri, destinationPath);

      if (type === 'entry') {
        await AsyncStorage.setItem('entrySoundPath', destinationPath);
      } else {
        await AsyncStorage.setItem('exitSoundPath', destinationPath);
      }

      Alert.alert('Success', `Custom ${type} sound set!`);
      loadCustomSounds();
    } catch (err) {
      if (!DocumentPicker.isCancel(err)) {
        console.error('File selection error:', err);
      }
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Notification Settings</Text>

      <View style={styles.setting}>
        <Text>Enable Sound</Text>
        <Switch value={soundEnabled} onValueChange={toggleSound} />
      </View>

      <View style={styles.setting}>
        <Text>Enable Vibration</Text>
        <Switch value={vibrationEnabled} onValueChange={toggleVibration} />
      </View>

      <View style={styles.setting}>
        <Text>Enable Entry Alert Sound</Text>
        <Switch value={entrySoundEnabled} onValueChange={toggleEntrySound} />
      </View>

      <View style={styles.setting}>
        <Text>Enable Exit Alert Sound</Text>
        <Switch value={exitSoundEnabled} onValueChange={toggleExitSound} />
      </View>

      <Button title="Choose Entry Sound" onPress={() => selectSoundFile('entry')} />
      <Button title="Choose Exit Sound" onPress={() => selectSoundFile('exit')} />
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: 'center', padding: 20 },
  title: { fontSize: 24, fontWeight: 'bold', textAlign: 'center', marginBottom: 20 },
  setting: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 20 }
});

export default NotificationSettingsScreen;
```
> **Users can now choose their own entry and exit sounds for alerts.**

---

## **4. Update Navigation**
Modify `App.js`:

```javascript
import NotificationSettingsScreen from './src/screens/NotificationSettingsScreen';

<Stack.Screen name="Notification Settings" component={NotificationSettingsScreen} />
```

---

## **5. Run and Test**
1. **Start React Native App**
   ```sh
   npx react-native run-android
   ```
   OR
   ```sh
   npx react-native run-ios
   ```
2. **Test Custom Sound Upload**
   - Go to **Notification Settings**.
   - Choose **a custom sound file** for entry and exit alerts.
   - Move a vehicle **inside or outside a geofence**.
   - Verify the **correct custom sound plays**.

---

## **6. Expected Behavior**
✅ **Users can upload custom sounds for entry/exit alerts**.  
✅ **Uploaded sounds replace default sounds** for alerts.  
✅ **Users can switch back to default sounds anytime**.  

---

## **7. Next Steps**
- Add **preview functionality** to test uploaded sounds.
- Implement **cloud storage to sync sounds across devices**.

Would you like me to implement **sound preview and cloud storage sync for custom sounds** next? 🚀
---
### **Implementing Sound Preview and Cloud Storage Sync for Custom Sounds**
To further enhance **custom sound selection for geofence alerts**, we will:
1. **Enable users to preview uploaded sounds before setting them**.
2. **Store and sync custom sounds via cloud storage (Firebase Storage)**.
3. **Modify the backend to retrieve stored sounds**.
4. **Ensure users can switch between local and cloud sounds**.

---

## **1. Install Firebase Storage for Cloud Sync**
We will use **Firebase Storage** to store and sync custom sounds.

```sh
npm install @react-native-firebase/storage
```
For **iOS**, run:
```sh
cd ios && pod install && cd ..
```

---

## **2. Configure Firebase in React Native**
Modify `android/build.gradle`:
```gradle
buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.3.10")
    }
}
```

Modify `android/app/build.gradle`:
```gradle
apply plugin: 'com.google.gms.google-services'
```

**Set up Firebase project** and download `google-services.json`, placing it in `android/app/`.

---

## **3. Modify Notification Service to Support Cloud Sounds**
Modify `src/services/notifications.js`:

```javascript
import { Vibration } from 'react-native';
import Sound from 'react-native-sound';
import AsyncStorage from '@react-native-async-storage/async-storage';
import RNFS from 'react-native-fs';
import storage from '@react-native-firebase/storage';

let entrySound;
let exitSound;

export const loadCustomSounds = async () => {
  const entrySoundPath = await AsyncStorage.getItem('entrySoundPath');
  const exitSoundPath = await AsyncStorage.getItem('exitSoundPath');
  const entryCloudSound = await AsyncStorage.getItem('entryCloudSound');
  const exitCloudSound = await AsyncStorage.getItem('exitCloudSound');

  entrySound = entryCloudSound
    ? new Sound(entryCloudSound, Sound.MAIN_BUNDLE, (error) => {
        if (error) console.error('Failed to load entry cloud sound', error);
      })
    : new Sound(entrySoundPath || 'entry.mp3', Sound.MAIN_BUNDLE);

  exitSound = exitCloudSound
    ? new Sound(exitCloudSound, Sound.MAIN_BUNDLE, (error) => {
        if (error) console.error('Failed to load exit cloud sound', error);
      })
    : new Sound(exitSoundPath || 'exit.mp3', Sound.MAIN_BUNDLE);
};

export const triggerLocalNotification = async (message, eventType) => {
  const soundEnabled = await AsyncStorage.getItem('soundEnabled');
  const vibrationEnabled = await AsyncStorage.getItem('vibrationEnabled');
  const entrySoundEnabled = await AsyncStorage.getItem('entrySoundEnabled');
  const exitSoundEnabled = await AsyncStorage.getItem('exitSoundEnabled');

  PushNotification.localNotification({
    title: 'Geofence Alert',
    message: message,
    playSound: soundEnabled !== 'false',
    soundName: 'default',
    vibrate: vibrationEnabled !== 'false',
    vibration: vibrationEnabled !== 'false' ? 500 : 0,
  });

  if (soundEnabled !== 'false') {
    if (eventType === 'ENTERED' && entrySoundEnabled !== 'false' && entrySound) {
      entrySound.play();
    } else if (eventType === 'EXITED' && exitSoundEnabled !== 'false' && exitSound) {
      exitSound.play();
    }
  }

  if (vibrationEnabled !== 'false') {
    Vibration.vibrate(500);
  }
};
```
> **This loads sounds from either local storage or Firebase Cloud Storage.**

---

## **4. Modify Settings to Preview Sounds and Upload to Cloud**
Modify `src/screens/NotificationSettingsScreen.js`:

```javascript
import React, { useEffect, useState } from 'react';
import { View, Text, Switch, Button, StyleSheet, Alert } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import DocumentPicker from 'react-native-document-picker';
import RNFS from 'react-native-fs';
import Sound from 'react-native-sound';
import storage from '@react-native-firebase/storage';
import { loadCustomSounds } from '../services/notifications';

const NotificationSettingsScreen = () => {
  const [soundEnabled, setSoundEnabled] = useState(true);
  const [vibrationEnabled, setVibrationEnabled] = useState(true);
  const [entrySoundEnabled, setEntrySoundEnabled] = useState(true);
  const [exitSoundEnabled, setExitSoundEnabled] = useState(true);

  useEffect(() => {
    loadPreferences();
  }, []);

  const loadPreferences = async () => {
    const sound = await AsyncStorage.getItem('soundEnabled');
    const vibration = await AsyncStorage.getItem('vibrationEnabled');
    const entrySound = await AsyncStorage.getItem('entrySoundEnabled');
    const exitSound = await AsyncStorage.getItem('exitSoundEnabled');

    setSoundEnabled(sound !== 'false');
    setVibrationEnabled(vibration !== 'false');
    setEntrySoundEnabled(entrySound !== 'false');
    setExitSoundEnabled(exitSound !== 'false');
  };

  const selectSoundFile = async (type) => {
    try {
      const res = await DocumentPicker.pickSingle({
        type: DocumentPicker.types.audio,
      });

      const destinationPath = `${RNFS.DocumentDirectoryPath}/${res.name}`;
      await RNFS.copyFile(res.uri, destinationPath);

      if (type === 'entry') {
        await AsyncStorage.setItem('entrySoundPath', destinationPath);
      } else {
        await AsyncStorage.setItem('exitSoundPath', destinationPath);
      }

      Alert.alert('Success', `Custom ${type} sound set!`);
      loadCustomSounds();
    } catch (err) {
      if (!DocumentPicker.isCancel(err)) {
        console.error('File selection error:', err);
      }
    }
  };

  const previewSound = async (type) => {
    const path = await AsyncStorage.getItem(type === 'entry' ? 'entrySoundPath' : 'exitSoundPath');
    if (!path) {
      Alert.alert('No Custom Sound', 'No custom sound has been set.');
      return;
    }
    const sound = new Sound(path, '', (error) => {
      if (error) {
        console.error('Error loading sound', error);
        Alert.alert('Error', 'Failed to play sound.');
        return;
      }
      sound.play();
    });
  };

  const uploadSoundToCloud = async (type) => {
    try {
      const localPath = await AsyncStorage.getItem(type === 'entry' ? 'entrySoundPath' : 'exitSoundPath');
      if (!localPath) {
        Alert.alert('No Custom Sound', 'Please select a custom sound first.');
        return;
      }

      const fileRef = storage().ref(`sounds/${type}_${Date.now()}.mp3`);
      await fileRef.putFile(localPath);
      const url = await fileRef.getDownloadURL();

      if (type === 'entry') {
        await AsyncStorage.setItem('entryCloudSound', url);
      } else {
        await AsyncStorage.setItem('exitCloudSound', url);
      }

      Alert.alert('Success', `Custom ${type} sound uploaded to cloud!`);
      loadCustomSounds();
    } catch (error) {
      console.error('Error uploading sound:', error);
      Alert.alert('Error', 'Failed to upload sound.');
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Notification Settings</Text>

      <View style={styles.setting}>
        <Text>Enable Sound</Text>
        <Switch value={soundEnabled} onValueChange={() => setSoundEnabled(!soundEnabled)} />
      </View>

      <Button title="Choose Entry Sound" onPress={() => selectSoundFile('entry')} />
      <Button title="Preview Entry Sound" onPress={() => previewSound('entry')} />
      <Button title="Upload Entry Sound to Cloud" onPress={() => uploadSoundToCloud('entry')} />

      <Button title="Choose Exit Sound" onPress={() => selectSoundFile('exit')} />
      <Button title="Preview Exit Sound" onPress={() => previewSound('exit')} />
      <Button title="Upload Exit Sound to Cloud" onPress={() => uploadSoundToCloud('exit')} />
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: 'center', padding: 20 },
  title: { fontSize: 24, fontWeight: 'bold', textAlign: 'center', marginBottom: 20 },
});

export default NotificationSettingsScreen;
```

---

## **Next Steps**
- Allow users to **share custom sounds with other users**.
- Implement **dynamic sound selection from cloud storage**.

Would you like me to implement **shared custom sounds across multiple users** next? 🚀
---
### **Implementing Shared Custom Sounds Across Multiple Users**
To allow users to **share custom geofence alert sounds**, we will:
1. **Enable users to share uploaded sounds via Firebase Storage**.
2. **Allow users to browse and select sounds uploaded by others**.
3. **Modify the backend to list all available sounds**.
4. **Update the React Native app to display shared sounds for selection**.

---

## **1. Modify Firebase Storage to Support Shared Sounds**
We will **store uploaded sounds in a shared Firebase Storage directory** and make them accessible to all users.

---

## **2. Modify React Native App to Upload & List Shared Sounds**
Modify `src/services/soundService.js`:

```javascript
import AsyncStorage from '@react-native-async-storage/async-storage';
import storage from '@react-native-firebase/storage';

export const uploadSoundToCloud = async (type) => {
  try {
    const localPath = await AsyncStorage.getItem(type === 'entry' ? 'entrySoundPath' : 'exitSoundPath');
    if (!localPath) {
      alert('Please select a custom sound first.');
      return;
    }

    const fileName = `${type}_${Date.now()}.mp3`;
    const fileRef = storage().ref(`shared_sounds/${fileName}`);
    await fileRef.putFile(localPath);
    const url = await fileRef.getDownloadURL();

    await AsyncStorage.setItem(type === 'entry' ? 'entryCloudSound' : 'exitCloudSound', url);
    alert(`Custom ${type} sound uploaded & shared!`);
  } catch (error) {
    console.error('Error uploading sound:', error);
    alert('Failed to upload sound.');
  }
};

export const getSharedSounds = async () => {
  try {
    const listRef = storage().ref('shared_sounds/');
    const fileList = await listRef.listAll();
    const soundUrls = await Promise.all(
      fileList.items.map(async (item) => {
        return { name: item.name, url: await item.getDownloadURL() };
      })
    );
    return soundUrls;
  } catch (error) {
    console.error('Error fetching shared sounds:', error);
    return [];
  }
};
```
> **Users can now upload sounds to a shared Firebase directory and fetch available shared sounds.**

---

## **3. Modify UI to Display Shared Sounds for Selection**
Modify `src/screens/SharedSoundsScreen.js`:

```javascript
import React, { useEffect, useState } from 'react';
import { View, Text, FlatList, Button, StyleSheet } from 'react-native';
import { getSharedSounds } from '../services/soundService';
import AsyncStorage from '@react-native-async-storage/async-storage';
import Sound from 'react-native-sound';

const SharedSoundsScreen = () => {
  const [sounds, setSounds] = useState([]);

  useEffect(() => {
    fetchSounds();
  }, []);

  const fetchSounds = async () => {
    const data = await getSharedSounds();
    setSounds(data);
  };

  const previewSound = (url) => {
    const sound = new Sound(url, '', (error) => {
      if (error) {
        console.error('Error loading sound', error);
        alert('Failed to play sound.');
        return;
      }
      sound.play();
    });
  };

  const selectSound = async (url, type) => {
    await AsyncStorage.setItem(type === 'entry' ? 'entryCloudSound' : 'exitCloudSound', url);
    alert(`Custom ${type} sound set!`);
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Shared Sounds</Text>

      <FlatList
        data={sounds}
        keyExtractor={(item) => item.name}
        renderItem={({ item }) => (
          <View style={styles.soundItem}>
            <Text>{item.name}</Text>
            <Button title="Preview" onPress={() => previewSound(item.url)} />
            <Button title="Set as Entry Sound" onPress={() => selectSound(item.url, 'entry')} />
            <Button title="Set as Exit Sound" onPress={() => selectSound(item.url, 'exit')} />
          </View>
        )}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, padding: 20 },
  title: { fontSize: 24, fontWeight: 'bold', textAlign: 'center', marginBottom: 20 },
  soundItem: { padding: 10, borderBottomWidth: 1 },
});

export default SharedSoundsScreen;
```
> **Users can browse, preview, and select shared sounds as entry or exit alerts.**

---

## **4. Update Navigation**
Modify `App.js`:

```javascript
import SharedSoundsScreen from './src/screens/SharedSoundsScreen';

<Stack.Screen name="Shared Sounds" component={SharedSoundsScreen} />
```

---

## **5. Run and Test**
1. **Start React Native App**
   ```sh
   npx react-native run-android
   ```
   OR
   ```sh
   npx react-native run-ios
   ```
2. **Test Shared Sounds**
   - Upload a **custom sound** from **Notification Settings**.
   - Visit the **Shared Sounds** screen.
   - **Preview a sound** before selecting it.
   - **Set it as an entry/exit sound**.
   - Trigger a **geofence event** and confirm the sound plays.

---

## **6. Expected Behavior**
✅ **Users can upload and share geofence alert sounds**.  
✅ **Shared sounds appear in the sound selection menu**.  
✅ **Users can preview and apply shared sounds instantly**.  

---

## **7. Next Steps**
- Allow **sound tagging (e.g., "Loud Alarm", "Soft Beep")** for better organization.
- Implement **search functionality** for shared sounds.

Would you like me to implement **sound tagging and search functionality for shared sounds** next? 🚀
---

