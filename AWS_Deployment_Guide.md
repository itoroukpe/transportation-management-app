### **🚀 AWS Cloud Deployment Guide for Transportation Management System (TMS)**  

This guide provides **step-by-step instructions** to deploy a **Spring Boot backend and React Native frontend** on **AWS** with scalable, secure, and high-availability infrastructure.

---

## **📌 1. AWS Services Overview for TMS Deployment**
| **Component** | **AWS Service** | **Purpose** |
|--------------|----------------|-------------|
| **Backend** | **AWS Elastic Beanstalk (ECS Fargate optional)** | Deploy scalable Spring Boot API |
| **Database** | **Amazon RDS (PostgreSQL)** | Store geofence, vehicle, and user data |
| **Real-Time Tracking** | **AWS IoT Core / Amazon Kinesis** | Handle vehicle location streaming |
| **Notifications** | **Amazon SNS / Firebase Cloud Messaging (FCM)** | Push alerts for geofence events |
| **File Storage** | **Amazon S3** | Store uploaded custom sounds |
| **Authentication** | **AWS Cognito** | Secure user login & JWT-based authentication |
| **Infrastructure** | **AWS EC2 / ECS (Fargate)** | Host scalable services |
| **Monitoring** | **Amazon CloudWatch** | Log monitoring & performance tracking |

---

## **📌 2. Deploy Spring Boot Backend on AWS**
### **🔹 Step 1: Package the Spring Boot Application**
Navigate to the backend folder and build the JAR file:
```sh
mvn clean package
```
This creates `target/app.jar`.

---

### **🔹 Step 2: Deploy to AWS Elastic Beanstalk**
#### **1️⃣ Install the AWS CLI & Elastic Beanstalk CLI**
```sh
pip install --upgrade awscli
pip install awsebcli --upgrade --user
```
#### **2️⃣ Configure AWS CLI (One-time setup)**
```sh
aws configure
```
Enter **AWS Access Key, Secret Key, Region, and Output format**.

#### **3️⃣ Create & Deploy an Elastic Beanstalk Environment**
Navigate to the project directory and run:
```sh
eb init -p java-17 my-tms-backend --region us-east-1
eb create tms-backend-env
```
This will:
✅ Set up an **EC2 instance** running your **Spring Boot API**  
✅ Create an **Elastic Load Balancer (ELB)** for auto-scaling  

---

### **🔹 Step 3: Connect Spring Boot to AWS RDS (PostgreSQL)**
#### **1️⃣ Create an RDS Database**
```sh
aws rds create-db-instance \
    --db-instance-identifier tms-db \
    --db-instance-class db.t3.micro \
    --engine postgres \
    --master-username admin \
    --master-user-password mySecurePass123 \
    --allocated-storage 20
```
**Modify `application.properties`:**
```properties
spring.datasource.url=jdbc:postgresql://tms-db.abcdefg.us-east-1.rds.amazonaws.com:5432/tmsdb
spring.datasource.username=admin
spring.datasource.password=mySecurePass123
```
Restart the application.

✅ **Database is now hosted on AWS RDS!**  

---

## **📌 3. Deploy React Native Frontend**
### **🔹 Step 1: Prepare Frontend for AWS Amplify**
Install AWS Amplify CLI:
```sh
npm install -g @aws-amplify/cli
amplify configure
```
Initialize the project:
```sh
amplify init
```
Add authentication:
```sh
amplify add auth
```
Push to AWS:
```sh
amplify push
```

---

### **🔹 Step 2: Deploy Frontend to AWS S3 & CloudFront**
```sh
amplify add hosting
amplify publish
```
This will:
✅ Deploy the frontend to **AWS S3**  
✅ Distribute via **CloudFront CDN** for global access  

---

## **📌 4. Set Up WebSockets for Real-Time Tracking**
Use **AWS IoT Core** for real-time vehicle tracking.

1️⃣ **Create an AWS IoT Core WebSocket API**:
```sh
aws iot create-thing --thing-name "VehicleTracker"
```
2️⃣ **Publish vehicle location updates**:
```sh
aws iot-data publish --topic "tms/vehicles/location" --payload "{'vehicleId': 1, 'lat': 37.7749, 'long': -122.4194}"
```

✅ **Real-time tracking enabled via AWS IoT Core!**  

---

## **📌 5. Set Up Push Notifications**
Use **Amazon SNS** to send geofence alerts.

### **🔹 Step 1: Create an SNS Topic**
```sh
aws sns create-topic --name GeofenceAlerts
```
### **🔹 Step 2: Subscribe to the Topic**
```sh
aws sns subscribe --topic-arn arn:aws:sns:us-east-1:123456789012:GeofenceAlerts --protocol application --notification-endpoint YOUR_FCM_APP_ARN
```
### **🔹 Step 3: Publish an Alert**
```sh
aws sns publish --topic-arn arn:aws:sns:us-east-1:123456789012:GeofenceAlerts --message "Vehicle entered geofence"
```
✅ **Push notifications are now integrated!**  

---

## **📌 6. Enable Logging & Monitoring**
Set up **CloudWatch** for monitoring.

### **🔹 Step 1: Enable CloudWatch Logs**
```sh
aws logs create-log-group --log-group-name /aws/beanstalk/tms-backend
aws logs create-log-stream --log-group-name /aws/beanstalk/tms-backend --log-stream-name backend-logs
```
### **🔹 Step 2: Enable CloudWatch Alarms**
```sh
aws cloudwatch put-metric-alarm --alarm-name "HighCPUUtilization" \
--metric-name CPUUtilization --namespace AWS/EC2 \
--statistic Average --period 300 --threshold 70 \
--comparison-operator GreaterThanThreshold --evaluation-periods 2 \
--alarm-actions arn:aws:sns:us-east-1:123456789012:GeofenceAlerts
```
✅ **Now the system automatically alerts on high CPU usage.**  

---

## **📌 7. Automate Deployment with AWS CodePipeline**
### **🔹 Step 1: Create an S3 Bucket for Artifacts**
```sh
aws s3 mb s3://tms-deployments
```
### **🔹 Step 2: Create a CodePipeline**
```sh
aws codepipeline create-pipeline --cli-input-json file://pipeline.json
```
**pipeline.json:**
```json
{
    "name": "TMS-Backend-Pipeline",
    "roleArn": "arn:aws:iam::123456789012:role/AWSCodePipelineServiceRole",
    "artifactStore": { "type": "S3", "location": "tms-deployments" },
    "stages": [
        { "name": "Source", "actions": [
            { "name": "GitHubSource", "actionTypeId": {
                "category": "Source", "owner": "ThirdParty",
                "provider": "GitHub", "version": "1"
            }, "configuration": {
                "Owner": "YourGitHubUser",
                "Repo": "TMS-Backend", "Branch": "main",
                "OAuthToken": "yourGitHubToken"
            } }
        ] },
        { "name": "Deploy", "actions": [
            { "name": "DeployToBeanstalk", "actionTypeId": {
                "category": "Deploy", "owner": "AWS",
                "provider": "ElasticBeanstalk", "version": "1"
            }, "configuration": {
                "ApplicationName": "tms-backend",
                "EnvironmentName": "tms-backend-env"
            } }
        ] }
    ]
}
```
✅ **Now every GitHub push triggers an automatic deployment!**  

---

### **🚀 Next Steps**
Would you like:
✅ **Terraform script for Infrastructure as Code (IaC)?**  
✅ **Auto-scaling for high traffic management?**  

Let me know how I can assist further! 🚀📡
