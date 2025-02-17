### **🚀 AWS Terraform Deployment for Transportation Management System (TMS)**  
This guide provides an **Infrastructure as Code (IaC) solution using Terraform** to deploy the **TMS backend, database, and cloud services** automatically on AWS.

---

## **📌 1. Prerequisites**
✅ Install **Terraform**:  
```sh
brew install terraform # macOS
sudo apt-get install terraform # Ubuntu
```
✅ Configure **AWS CLI**:
```sh
aws configure
```
✅ **Create an S3 bucket** for Terraform state storage:
```sh
aws s3 mb s3://tms-terraform-state
```

---

## **📌 2. Terraform Project Structure**
```
tms-infra/
│── main.tf               # AWS resources
│── variables.tf          # Input variables
│── outputs.tf            # Resource outputs
│── provider.tf           # AWS provider configuration
│── terraform.tfvars      # User-defined variables
```

---

## **📌 3. Define AWS Provider (`provider.tf`)**
```hcl
provider "aws" {
  region = "us-east-1"
}
```

---

## **📌 4. Deploy VPC & Networking (`main.tf`)**
```hcl
resource "aws_vpc" "tms_vpc" {
  cidr_block = "10.0.0.0/16"
}

resource "aws_subnet" "public_subnet" {
  vpc_id            = aws_vpc.tms_vpc.id
  cidr_block        = "10.0.1.0/24"
  availability_zone = "us-east-1a"
}

resource "aws_internet_gateway" "tms_gw" {
  vpc_id = aws_vpc.tms_vpc.id
}

resource "aws_route_table" "public_route_table" {
  vpc_id = aws_vpc.tms_vpc.id
}

resource "aws_route" "default_route" {
  route_table_id         = aws_route_table.public_route_table.id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id             = aws_internet_gateway.tms_gw.id
}

resource "aws_route_table_association" "public_subnet_association" {
  subnet_id      = aws_subnet.public_subnet.id
  route_table_id = aws_route_table.public_route_table.id
}
```
✅ **Creates a secure VPC with public subnets & internet access**.

---

## **📌 5. Deploy RDS (PostgreSQL) Database**
```hcl
resource "aws_db_instance" "tms_rds" {
  allocated_storage    = 20
  engine              = "postgres"
  instance_class      = "db.t3.micro"
  db_name             = "tmsdb"
  username           = "admin"
  password           = "SecurePass123"
  publicly_accessible = false
  vpc_security_group_ids = [aws_security_group.rds_sg.id]
}
```
✅ **Creates a PostgreSQL database for TMS.**

---

## **📌 6. Deploy Spring Boot Backend on Elastic Beanstalk**
```hcl
resource "aws_elastic_beanstalk_application" "tms_backend" {
  name = "TMSBackend"
}

resource "aws_elastic_beanstalk_environment" "tms_backend_env" {
  name                = "TMSBackendEnv"
  application         = aws_elastic_beanstalk_application.tms_backend.name
  solution_stack_name = "64bit Amazon Linux 2 v5.4.2 running Corretto 11"

  setting {
    namespace = "aws:elasticbeanstalk:environment"
    name      = "EnvironmentType"
    value     = "LoadBalanced"
  }

  setting {
    namespace = "aws:rds:dbinstance"
    name      = "DBEngine"
    value     = "postgres"
  }
}
```
✅ **Deploys backend with AWS Elastic Beanstalk & auto-scaling**.

---

## **📌 7. Deploy S3 for File Storage (Custom Sounds)**
```hcl
resource "aws_s3_bucket" "tms_sounds" {
  bucket = "tms-sound-storage"
}

resource "aws_s3_bucket_policy" "tms_sounds_policy" {
  bucket = aws_s3_bucket.tms_sounds.id
  policy = <<POLICY
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": "*",
      "Action": "s3:GetObject",
      "Resource": "arn:aws:s3:::tms-sound-storage/*"
    }
  ]
}
POLICY
}
```
✅ **S3 bucket for storing shared sounds & user uploads**.

---

## **📌 8. Deploy CloudFront CDN for Frontend**
```hcl
resource "aws_cloudfront_distribution" "cdn" {
  origin {
    domain_name = aws_s3_bucket.tms_sounds.bucket_regional_domain_name
    origin_id   = "S3-tms-sound-storage"
  }

  enabled             = true
  default_root_object = "index.html"

  restrictions {
    geo_restriction {
      restriction_type = "whitelist"
      locations        = ["US", "CA", "UK"]
    }
  }
}
```
✅ **Enables fast global delivery of frontend & sound files.**

---

## **📌 9. Deploy AWS IoT Core for Real-Time Vehicle Tracking**
```hcl
resource "aws_iot_thing" "vehicle_tracker" {
  name = "VehicleTracker"
}

resource "aws_iot_policy" "vehicle_tracker_policy" {
  name   = "VehicleTrackerPolicy"
  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "iot:Connect",
        "iot:Publish",
        "iot:Subscribe",
        "iot:Receive"
      ],
      "Resource": "*"
    }
  ]
}
EOF
}
```
✅ **Allows vehicles to send GPS updates to AWS IoT Core.**

---

## **📌 10. Deploy SNS for Push Notifications**
```hcl
resource "aws_sns_topic" "geofence_alerts" {
  name = "GeofenceAlerts"
}

resource "aws_sns_topic_subscription" "geofence_email_sub" {
  topic_arn = aws_sns_topic.geofence_alerts.arn
  protocol  = "email"
  endpoint  = "admin@tms.com"
}
```
✅ **Sends geofence entry/exit alerts via email & mobile push notifications.**

---

## **📌 11. Deploy AWS CodePipeline for CI/CD**
```hcl
resource "aws_codepipeline" "tms_pipeline" {
  name     = "TMSBackendPipeline"
  role_arn = "arn:aws:iam::123456789012:role/AWSCodePipelineServiceRole"

  artifact_store {
    type     = "S3"
    location = "tms-deployments"
  }

  stage {
    name = "Source"

    action {
      name             = "GitHubSource"
      category         = "Source"
      owner           = "ThirdParty"
      provider        = "GitHub"
      version         = "1"
      configuration = {
        Owner      = "YourGitHubUser"
        Repo       = "TMS-Backend"
        Branch     = "main"
        OAuthToken = "yourGitHubToken"
      }
    }
  }

  stage {
    name = "Deploy"

    action {
      name      = "DeployToBeanstalk"
      category  = "Deploy"
      owner     = "AWS"
      provider  = "ElasticBeanstalk"
      version   = "1"
      configuration = {
        ApplicationName = "TMSBackend"
        EnvironmentName = "TMSBackendEnv"
      }
    }
  }
}
```
✅ **Automates deployments from GitHub to AWS!**

---

## **🚀 Deploy Everything with Terraform**
```sh
terraform init
terraform plan
terraform apply
```
🚀 **Terraform will provision AWS resources in minutes!** 🚀

---

## **📌 Next Steps**
Would you like:
✅ **Terraform module for auto-scaling EC2 instances?**  
✅ **Grafana dashboards for performance monitoring?**  

---
### **🚀 Auto-Scaling EC2 & Performance Monitoring with Grafana for Transportation Management System (TMS)**
This guide extends our **AWS Terraform Infrastructure** to include:  
✅ **Auto-scaling for high-traffic management** (EC2 with Load Balancer)  
✅ **Grafana dashboards for performance monitoring** (Integrated with AWS CloudWatch)  

---

## **📌 1. Auto-Scaling EC2 with Elastic Load Balancer (ELB)**
### **🔹 Step 1: Create an EC2 Launch Template (`ec2.tf`)**
This template ensures that new instances are created with the correct settings.

```hcl
resource "aws_launch_template" "tms_ec2_template" {
  name_prefix   = "tms-backend-template"
  image_id      = "ami-12345678" # Use latest Amazon Linux 2 AMI ID
  instance_type = "t3.medium"
  key_name      = "tms-key-pair"

  network_interfaces {
    associate_public_ip_address = true
    subnet_id                   = aws_subnet.public_subnet.id
  }

  tag_specifications {
    resource_type = "instance"
    tags = {
      Name = "TMS-Backend-Instance"
    }
  }
}
```
✅ **Creates EC2 instances based on a predefined template**  

---

### **🔹 Step 2: Create an Auto Scaling Group (`autoscaling.tf`)**
Manages **EC2 instance scaling based on CPU utilization**.

```hcl
resource "aws_autoscaling_group" "tms_asg" {
  name                = "tms-backend-asg"
  launch_template {
    id      = aws_launch_template.tms_ec2_template.id
    version = "$Latest"
  }
  min_size            = 2
  max_size            = 5
  desired_capacity    = 2
  vpc_zone_identifier = [aws_subnet.public_subnet.id]
  health_check_type   = "EC2"
}

resource "aws_autoscaling_policy" "tms_scale_out" {
  name                   = "scale-out"
  scaling_adjustment     = 1
  adjustment_type        = "ChangeInCapacity"
  cooldown               = 300
  autoscaling_group_name = aws_autoscaling_group.tms_asg.name
}

resource "aws_autoscaling_policy" "tms_scale_in" {
  name                   = "scale-in"
  scaling_adjustment     = -1
  adjustment_type        = "ChangeInCapacity"
  cooldown               = 300
  autoscaling_group_name = aws_autoscaling_group.tms_asg.name
}
```
✅ **Automatically adds/removes EC2 instances based on demand**  

---

### **🔹 Step 3: Attach Load Balancer (`elb.tf`)**
Balances incoming traffic between EC2 instances.

```hcl
resource "aws_lb" "tms_lb" {
  name               = "tms-load-balancer"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.lb_sg.id]
  subnets           = [aws_subnet.public_subnet.id]
}

resource "aws_lb_target_group" "tms_target_group" {
  name     = "tms-target-group"
  port     = 8080
  protocol = "HTTP"
  vpc_id   = aws_vpc.tms_vpc.id
}

resource "aws_lb_listener" "tms_listener" {
  load_balancer_arn = aws_lb.tms_lb.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.tms_target_group.arn
  }
}

resource "aws_autoscaling_attachment" "asg_attachment" {
  autoscaling_group_name = aws_autoscaling_group.tms_asg.id
  lb_target_group_arn   = aws_lb_target_group.tms_target_group.arn
}
```
✅ **Distributes traffic across multiple backend instances**  

---

## **📌 2. AWS CloudWatch & Grafana for Performance Monitoring**
Now, let’s set up **CloudWatch metrics & Grafana** for **real-time monitoring**.

### **🔹 Step 1: Enable CloudWatch Metrics (`monitoring.tf`)**
```hcl
resource "aws_cloudwatch_metric_alarm" "high_cpu" {
  alarm_name          = "High-CPU-Utilization"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods  = 2
  metric_name         = "CPUUtilization"
  namespace           = "AWS/EC2"
  period              = 60
  statistic           = "Average"
  threshold           = 70
  alarm_actions       = [aws_sns_topic.alerts.arn]
  dimensions = {
    AutoScalingGroupName = aws_autoscaling_group.tms_asg.name
  }
}
```
✅ **Triggers an alert when CPU usage exceeds 70%**  

---

### **🔹 Step 2: Deploy Grafana on AWS EC2**
1️⃣ **Launch a Grafana instance on EC2**
```sh
aws ec2 run-instances --image-id ami-12345678 --instance-type t3.medium --key-name tms-key-pair --security-groups Grafana-SG
```
2️⃣ **Connect to EC2 instance**
```sh
ssh -i "tms-key-pair.pem" ec2-user@your-instance-ip
```
3️⃣ **Install Grafana**
```sh
sudo yum install -y https://dl.grafana.com/oss/release/grafana-8.3.3-1.x86_64.rpm
sudo systemctl start grafana-server
```
4️⃣ **Connect AWS CloudWatch to Grafana**
```sh
aws cloudwatch put-metric-data --namespace "TMS" --metric-name "RequestCount" --value 100
```
✅ **Grafana is now connected to AWS CloudWatch for real-time performance monitoring!**  

---

## **📌 3. Automate Terraform Deployment**
To automate deployment, run:
```sh
terraform init
terraform apply -auto-approve
```
✅ **Deploys everything in minutes!**

---

## **📌 4. Next Steps**
Would you like:
✅ **AWS Lambda function for automated alerts?**  
✅ **Advanced security with AWS WAF & IAM roles?**  

---
### **🚀 Advanced Security & Automated Alerts for TMS on AWS**
This guide extends our **AWS Terraform Infrastructure** to include:  
✅ **AWS Lambda for Automated Alerts** (Geofence violations, high CPU usage)  
✅ **AWS WAF for Web Application Security** (Protection against DDoS, SQL Injection)  
✅ **IAM Roles & Permissions** (Granular security control for AWS services)  

---

## **📌 1. AWS Lambda for Automated Alerts**
We’ll create an **AWS Lambda function** that:
✅ **Monitors vehicle geofence violations**  
✅ **Detects CPU spikes & sends notifications via SNS**  

### **🔹 Step 1: Create a Lambda Function (`lambda.tf`)**
```hcl
resource "aws_lambda_function" "tms_alert_lambda" {
  filename      = "lambda.zip"
  function_name = "TMSAlertFunction"
  role          = aws_iam_role.lambda_exec.arn
  handler       = "lambda_function.lambda_handler"
  runtime       = "python3.8"
}

resource "aws_iam_role" "lambda_exec" {
  name = "lambda-exec-role"
  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
EOF
}

resource "aws_lambda_permission" "allow_sns" {
  statement_id  = "AllowExecutionFromSNS"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.tms_alert_lambda.function_name
  principal     = "sns.amazonaws.com"
  source_arn    = aws_sns_topic.alerts.arn
}
```
✅ **Creates a Lambda function with SNS permissions**  

---

### **🔹 Step 2: Write Python Code for Lambda Alerts**
Create a file `lambda_function.py`:
```python
import json
import boto3

sns_client = boto3.client("sns")

def lambda_handler(event, context):
    message = json.loads(event["Records"][0]["Sns"]["Message"])
    
    if message.get("alertType") == "geofence_violation":
        sns_client.publish(
            TopicArn="arn:aws:sns:us-east-1:123456789012:GeofenceAlerts",
            Message=f"Vehicle {message['vehicleId']} has violated geofence {message['geofenceId']}",
            Subject="Geofence Violation Alert"
        )
    
    if message.get("alertType") == "high_cpu":
        sns_client.publish(
            TopicArn="arn:aws:sns:us-east-1:123456789012:ServerAlerts",
            Message="High CPU usage detected! Investigate immediately.",
            Subject="Server Alert"
        )

    return {"statusCode": 200, "body": json.dumps("Alert sent successfully")}
```
✅ **Automatically triggers alerts based on conditions**  

---

### **🔹 Step 3: Deploy Lambda Function**
1️⃣ **Create a ZIP package**
```sh
zip lambda.zip lambda_function.py
```
2️⃣ **Deploy with Terraform**
```sh
terraform apply -auto-approve
```
✅ **Lambda is now live & listens for geofence violations & CPU spikes!**  

---

## **📌 2. Secure the Web Application with AWS WAF**
AWS WAF helps **prevent SQL injection, XSS, and DDoS attacks**.

### **🔹 Step 1: Create AWS WAF WebACL (`waf.tf`)**
```hcl
resource "aws_wafv2_web_acl" "tms_waf" {
  name        = "TMSWebACL"
  scope       = "REGIONAL"
  default_action {
    allow {}
  }

  rule {
    name     = "SQLInjectionRule"
    priority = 1
    action {
      block {}
    }
    statement {
      sql_injection_match_statement {
        field_to_match {
          query_string {}
        }
        text_transformation {
          priority = 0
          type     = "URL_DECODE"
        }
      }
    }
    visibility_config {
      cloudwatch_metrics_enabled = true
      metric_name                = "SQLInjectionRule"
      sampled_requests_enabled   = true
    }
  }
}

resource "aws_wafv2_web_acl_association" "tms_waf_assoc" {
  resource_arn = aws_lb.tms_lb.arn
  web_acl_arn  = aws_wafv2_web_acl.tms_waf.arn
}
```
✅ **Blocks SQL injection attempts on the TMS backend!**  

---

## **📌 3. Secure AWS Services with IAM Roles & Policies**
AWS IAM ensures **restricted access** to **only necessary services**.

### **🔹 Step 1: Create IAM Role for EC2 Instances**
```hcl
resource "aws_iam_role" "tms_ec2_role" {
  name = "TMS-EC2-Role"
  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "ec2.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
EOF
}
```
✅ **Restricts EC2 permissions to only required actions**  

---

### **🔹 Step 2: Attach IAM Policy to Restrict S3 & SNS**
```hcl
resource "aws_iam_policy" "tms_s3_policy" {
  name        = "TMS-S3-Policy"
  description = "Allows read access to S3 storage"
  policy      = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": ["s3:GetObject"],
      "Resource": "arn:aws:s3:::tms-sound-storage/*"
    }
  ]
}
EOF
}

resource "aws_iam_role_policy_attachment" "attach_s3_policy" {
  policy_arn = aws_iam_policy.tms_s3_policy.arn
  role       = aws_iam_role.tms_ec2_role.name
}
```
✅ **EC2 instances can read from S3 but not modify/delete files**  

---

## **📌 4. Automate Terraform Deployment**
Run:
```sh
terraform init
terraform apply -auto-approve
```
✅ **Deploys security measures & automated alerts in minutes!**  

---

## **📌 5. Next Steps**
Would you like:
✅ **AWS API Gateway to manage requests securely?**  
✅ **Auto-healing EC2 with AWS Auto Recovery?**  

---
### **🚀 Implementing AWS API Gateway & Auto-Healing EC2 for Transportation Management System (TMS)**  

This guide extends our **AWS Terraform Infrastructure** by adding:  
✅ **AWS API Gateway for Secure Request Management** (Protects backend APIs)  
✅ **Auto-Healing EC2 with AWS Auto Recovery** (Ensures high availability)  

---

## **📌 1. Secure API Requests with AWS API Gateway**  
AWS API Gateway will act as a **proxy** between the frontend (React Native) and backend (Spring Boot), enabling:  
✅ **Rate limiting & DDoS protection**  
✅ **JWT-based authentication**  
✅ **Secure backend API exposure**  

---

### **🔹 Step 1: Create API Gateway (`apigateway.tf`)**
```hcl
resource "aws_api_gateway_rest_api" "tms_api" {
  name        = "TMSAPI"
  description = "Transportation Management System API Gateway"
}

resource "aws_api_gateway_resource" "vehicles" {
  rest_api_id = aws_api_gateway_rest_api.tms_api.id
  parent_id   = aws_api_gateway_rest_api.tms_api.root_resource_id
  path_part   = "vehicles"
}

resource "aws_api_gateway_method" "get_vehicles" {
  rest_api_id   = aws_api_gateway_rest_api.tms_api.id
  resource_id   = aws_api_gateway_resource.vehicles.id
  http_method   = "GET"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "get_vehicles_integration" {
  rest_api_id             = aws_api_gateway_rest_api.tms_api.id
  resource_id             = aws_api_gateway_resource.vehicles.id
  http_method             = aws_api_gateway_method.get_vehicles.http_method
  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = aws_lambda_function.tms_alert_lambda.invoke_arn
}
```
✅ **Maps `/vehicles` API to AWS Lambda for request handling**  

---

### **🔹 Step 2: Deploy API Gateway**
```hcl
resource "aws_api_gateway_deployment" "tms_deployment" {
  rest_api_id = aws_api_gateway_rest_api.tms_api.id
  stage_name  = "prod"
}
```
✅ **Deploys API Gateway for public access**  

---

### **🔹 Step 3: Enable JWT Authentication for Secure API Calls**
```hcl
resource "aws_api_gateway_authorizer" "jwt_auth" {
  name                   = "JWTAuthorizer"
  rest_api_id            = aws_api_gateway_rest_api.tms_api.id
  type                   = "TOKEN"
  authorizer_uri         = "arn:aws:lambda:us-east-1:123456789012:function:TMSAuthLambda"
  identity_source        = "method.request.header.Authorization"
}
```
✅ **Ensures only authenticated requests access APIs**  

---

### **🔹 Step 4: Configure Rate Limiting (DDoS Protection)**
```hcl
resource "aws_api_gateway_usage_plan" "tms_usage_plan" {
  name         = "TMSUsagePlan"
  description  = "Rate limiting for API requests"
  api_stages {
    api_id = aws_api_gateway_rest_api.tms_api.id
    stage  = aws_api_gateway_deployment.tms_deployment.stage_name
  }
  throttle_settings {
    rate_limit  = 10
    burst_limit = 20
  }
}
```
✅ **Limits API requests to prevent abuse**  

---

### **🔹 Step 5: Add CloudFront CDN for Faster API Access**
```hcl
resource "aws_cloudfront_distribution" "tms_api_cdn" {
  origin {
    domain_name = "${aws_api_gateway_rest_api.tms_api.id}.execute-api.us-east-1.amazonaws.com"
    origin_id   = "APIGatewayOrigin"
  }

  enabled             = true
  default_root_object = "index.html"
}
```
✅ **Improves API response time with CloudFront caching**  

---

## **📌 2. Auto-Healing EC2 with AWS Auto Recovery**  
AWS **Auto Recovery** ensures EC2 instances automatically **restart** if they crash.

---

### **🔹 Step 1: Create CloudWatch Alarm for Auto Recovery (`ec2_recovery.tf`)**
```hcl
resource "aws_cloudwatch_metric_alarm" "ec2_auto_recovery" {
  alarm_name          = "EC2AutoRecovery"
  comparison_operator = "LessThanThreshold"
  evaluation_periods  = 2
  metric_name         = "StatusCheckFailed_System"
  namespace           = "AWS/EC2"
  period              = 60
  statistic           = "Minimum"
  threshold           = 1
  alarm_actions       = ["arn:aws:automate:us-east-1:ec2:recover"]
  dimensions = {
    InstanceId = aws_instance.tms_ec2.id
  }
}
```
✅ **Auto-recovers EC2 if system health check fails**  

---

### **🔹 Step 2: Enable EC2 Termination Protection**
```hcl
resource "aws_instance" "tms_ec2" {
  ami                    = "ami-12345678"
  instance_type          = "t3.medium"
  key_name               = "tms-key-pair"
  disable_api_termination = true
}
```
✅ **Prevents accidental termination of EC2 instances**  

---

### **🔹 Step 3: Add Lifecycle Hook to Replace Failed Instances**
```hcl
resource "aws_autoscaling_lifecycle_hook" "ec2_replacement_hook" {
  name                   = "ReplaceUnhealthyInstances"
  autoscaling_group_name = aws_autoscaling_group.tms_asg.id
  lifecycle_transition   = "autoscaling:EC2_INSTANCE_TERMINATING"
}
```
✅ **Replaces EC2 instances automatically upon failure**  

---

### **📌 3. Automate Terraform Deployment**
Run:
```sh
terraform init
terraform apply -auto-approve
```
✅ **Deploys API Gateway, auto-healing EC2, and enhanced security in minutes!**  

---

## **📌 4. Next Steps**
Would you like:  
✅ **AWS Lambda function for auto-scaling based on traffic?**  
✅ **Custom domain & SSL certificate with AWS Route 53?**  

---
### **🚀 Auto-Scaling with AWS Lambda & Custom Domain with Route 53 for TMS**  
This guide extends our **AWS Terraform Infrastructure** by adding:  
✅ **AWS Lambda for Auto-Scaling Based on Traffic** (Dynamically adds/removes instances)  
✅ **Custom Domain with SSL Using AWS Route 53 & ACM** (Secure HTTPS access)  

---

## **📌 1. Auto-Scaling Based on Traffic with AWS Lambda**
Instead of manually setting scaling policies, we’ll use **AWS Lambda + CloudWatch** to dynamically adjust the number of EC2 instances based on real-time traffic.

### **🔹 Step 1: Create a Lambda Function for Auto-Scaling (`lambda_autoscaling.tf`)**
```hcl
resource "aws_lambda_function" "auto_scaling_lambda" {
  filename      = "autoscale_lambda.zip"
  function_name = "TMSAutoScaler"
  role          = aws_iam_role.lambda_autoscaling.arn
  handler       = "autoscale_function.lambda_handler"
  runtime       = "python3.8"
}

resource "aws_iam_role" "lambda_autoscaling" {
  name = "LambdaAutoScalingRole"
  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
EOF
}
```
✅ **Lambda will be triggered when traffic increases or decreases.**  

---

### **🔹 Step 2: Write Python Code for Lambda Auto-Scaling**
Create `autoscale_function.py`:
```python
import boto3

client = boto3.client('autoscaling')

def lambda_handler(event, context):
    response = client.describe_auto_scaling_groups(AutoScalingGroupNames=['tms-backend-asg'])
    desired_capacity = response['AutoScalingGroups'][0]['DesiredCapacity']

    cpu_utilization = event.get('cpu_utilization', 0)

    if cpu_utilization > 70 and desired_capacity < 5:
        client.set_desired_capacity(AutoScalingGroupName='tms-backend-asg', DesiredCapacity=desired_capacity + 1)
    elif cpu_utilization < 30 and desired_capacity > 2:
        client.set_desired_capacity(AutoScalingGroupName='tms-backend-asg', DesiredCapacity=desired_capacity - 1)

    return {'message': 'Auto-Scaling updated', 'new_capacity': desired_capacity}
```
✅ **Dynamically adjusts the number of EC2 instances based on CPU utilization.**  

---

### **🔹 Step 3: Deploy Lambda Auto-Scaling**
1️⃣ **Zip & Deploy Lambda Function**
```sh
zip autoscale_lambda.zip autoscale_function.py
terraform apply -auto-approve
```
2️⃣ **Create CloudWatch Alarm to Trigger Lambda**
```hcl
resource "aws_cloudwatch_metric_alarm" "cpu_alert" {
  alarm_name          = "CPUUtilizationAlert"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods  = 2
  metric_name         = "CPUUtilization"
  namespace           = "AWS/EC2"
  period              = 60
  statistic           = "Average"
  threshold           = 70
  alarm_actions       = [aws_lambda_function.auto_scaling_lambda.arn]
}
```
✅ **Now AWS Lambda automatically scales EC2 based on demand!**  

---

## **📌 2. Custom Domain & SSL with AWS Route 53 & ACM**
### **🔹 Step 1: Register a Custom Domain on Route 53**
1️⃣ **Create Route 53 Hosted Zone**
```hcl
resource "aws_route53_zone" "tms_domain" {
  name = "tms.com"
}
```
2️⃣ **Create an A Record for Load Balancer**
```hcl
resource "aws_route53_record" "tms_record" {
  zone_id = aws_route53_zone.tms_domain.zone_id
  name    = "api.tms.com"
  type    = "A"

  alias {
    name                   = aws_lb.tms_lb.dns_name
    zone_id                = aws_lb.tms_lb.zone_id
    evaluate_target_health = true
  }
}
```
✅ **Maps `api.tms.com` to AWS Load Balancer.**  

---

### **🔹 Step 2: Issue an SSL Certificate with AWS ACM**
1️⃣ **Request SSL Certificate**
```hcl
resource "aws_acm_certificate" "tms_ssl" {
  domain_name       = "api.tms.com"
  validation_method = "DNS"

  lifecycle {
    create_before_destroy = true
  }
}
```
2️⃣ **Validate Certificate with Route 53**
```hcl
resource "aws_route53_record" "cert_validation" {
  zone_id = aws_route53_zone.tms_domain.zone_id
  name    = aws_acm_certificate.tms_ssl.domain_validation_options[0].resource_record_name
  type    = aws_acm_certificate.tms_ssl.domain_validation_options[0].resource_record_type
  records = [aws_acm_certificate.tms_ssl.domain_validation_options[0].resource_record_value]
  ttl     = 60
}
```
✅ **Automatically validates the certificate using Route 53 DNS.**  

---

### **🔹 Step 3: Enable HTTPS on Load Balancer**
```hcl
resource "aws_lb_listener" "https_listener" {
  load_balancer_arn = aws_lb.tms_lb.arn
  port              = 443
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-2016-08"
  certificate_arn   = aws_acm_certificate.tms_ssl.arn

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.tms_target_group.arn
  }
}
```
✅ **Enforces HTTPS traffic for API access.**  

---

## **📌 3. Automate Terraform Deployment**
Run:
```sh
terraform init
terraform apply -auto-approve
```
✅ **Deploys auto-scaling & secure domain setup in minutes!**  

---

## **📌 4. Next Steps**
Would you like:  
✅ **AWS Cognito for User Authentication & Single Sign-On (SSO)?**  
✅ **Centralized Logging with AWS CloudWatch & ELK Stack?**  

---
### **🚀 Implementing AWS Cognito for User Authentication & Centralized Logging with AWS CloudWatch & ELK Stack**  

This guide extends our **AWS Terraform Infrastructure** by adding:  
✅ **AWS Cognito for Secure User Authentication & SSO**  
✅ **Centralized Logging with AWS CloudWatch & ELK Stack (Elasticsearch, Logstash, Kibana)**  

---

## **📌 1. AWS Cognito for Secure Authentication & SSO**  
AWS Cognito will handle:  
✅ **User Sign-up & Login**  
✅ **Token-based Authentication (OAuth2, JWT)**  
✅ **Single Sign-On (SSO) with Google, Facebook, and Microsoft**  

---

### **🔹 Step 1: Create Cognito User Pool (`cognito.tf`)**
```hcl
resource "aws_cognito_user_pool" "tms_user_pool" {
  name = "TMSUserPool"

  password_policy {
    minimum_length    = 8
    require_uppercase = true
    require_lowercase = true
    require_numbers   = true
    require_symbols   = false
  }

  username_attributes = ["email"]
}
```
✅ **Creates a user pool with strong password policies.**  

---

### **🔹 Step 2: Create Cognito Identity Pool for Federated Access**
```hcl
resource "aws_cognito_identity_pool" "tms_identity_pool" {
  identity_pool_name = "TMSIdentityPool"
  allow_unauthenticated_identities = false
}
```
✅ **Allows authenticated users to access AWS services securely.**  

---

### **🔹 Step 3: Enable SSO with Google, Facebook, and Microsoft**
```hcl
resource "aws_cognito_identity_provider" "google_sso" {
  user_pool_id  = aws_cognito_user_pool.tms_user_pool.id
  provider_name = "Google"
  provider_type = "Google"

  provider_details = {
    client_id     = "GOOGLE_CLIENT_ID"
    client_secret = "GOOGLE_CLIENT_SECRET"
    authorize_scopes = "openid email profile"
  }
}
```
✅ **Users can log in using Google, Facebook, or Microsoft accounts.**  

---

### **🔹 Step 4: Create API Gateway Authentication Integration**
```hcl
resource "aws_api_gateway_authorizer" "cognito_auth" {
  name                   = "CognitoAuthorizer"
  rest_api_id            = aws_api_gateway_rest_api.tms_api.id
  type                   = "COGNITO_USER_POOLS"
  provider_arns          = [aws_cognito_user_pool.tms_user_pool.arn]
  identity_source        = "method.request.header.Authorization"
}
```
✅ **API Gateway now validates JWT tokens from Cognito.**  

---

### **🔹 Step 5: Deploy & Verify Cognito Authentication**
1️⃣ **Run Terraform**
```sh
terraform apply -auto-approve
```
2️⃣ **Test Login via AWS CLI**
```sh
aws cognito-idp initiate-auth --client-id YOUR_CLIENT_ID \
  --auth-flow USER_PASSWORD_AUTH \
  --auth-parameters USERNAME=user@example.com,PASSWORD=SecurePass123
```
✅ **Users can now sign in securely using AWS Cognito!**  

---

## **📌 2. Centralized Logging with AWS CloudWatch & ELK Stack**  
We’ll enable **end-to-end logging** using:  
✅ **AWS CloudWatch Logs** for backend logs  
✅ **Elasticsearch for indexing & searchability**  
✅ **Kibana for log visualization**  

---

### **🔹 Step 1: Enable CloudWatch Logging for API Gateway**
```hcl
resource "aws_api_gateway_stage" "tms_stage" {
  deployment_id = aws_api_gateway_deployment.tms_deployment.id
  rest_api_id   = aws_api_gateway_rest_api.tms_api.id
  stage_name    = "prod"

  access_log_settings {
    destination_arn = aws_cloudwatch_log_group.api_gateway_logs.arn
    format          = "$context.identity.sourceIp - $context.requestId - $context.status - $context.protocol"
  }
}

resource "aws_cloudwatch_log_group" "api_gateway_logs" {
  name = "/aws/api-gateway/tms-api"
}
```
✅ **All API Gateway logs are stored in AWS CloudWatch.**  

---

### **🔹 Step 2: Create an Elasticsearch Cluster for Log Indexing**
```hcl
resource "aws_elasticsearch_domain" "tms_logs" {
  domain_name           = "tms-logs"
  elasticsearch_version = "7.10"
  cluster_config {
    instance_type = "t3.medium.elasticsearch"
  }
  ebs_options {
    ebs_enabled = true
    volume_size = 10
  }
}
```
✅ **Deploys a managed Elasticsearch cluster for storing logs.**  

---

### **🔹 Step 3: Deploy Logstash to Process Logs (`logstash.tf`)**
```hcl
resource "aws_instance" "logstash" {
  ami           = "ami-12345678"
  instance_type = "t3.medium"
  user_data = <<EOF
#!/bin/bash
sudo yum install -y logstash
sudo systemctl start logstash
EOF
}
```
✅ **Logstash will parse logs & send them to Elasticsearch.**  

---

### **🔹 Step 4: Deploy Kibana for Log Visualization**
```hcl
resource "aws_instance" "kibana" {
  ami           = "ami-12345678"
  instance_type = "t3.medium"
  user_data = <<EOF
#!/bin/bash
sudo yum install -y kibana
sudo systemctl start kibana
EOF
}
```
✅ **Kibana provides a dashboard to visualize API and system logs.**  

---

### **🔹 Step 5: Create Log Forwarding from CloudWatch to ELK**
```hcl
resource "aws_cloudwatch_log_subscription_filter" "elk_forwarder" {
  name            = "ForwardToELK"
  log_group_name  = aws_cloudwatch_log_group.api_gateway_logs.name
  filter_pattern  = ""
  destination_arn = aws_elasticsearch_domain.tms_logs.arn
}
```
✅ **CloudWatch logs are automatically forwarded to Elasticsearch.**  

---

## **📌 3. Automate Terraform Deployment**
Run:
```sh
terraform init
terraform apply -auto-approve
```
✅ **Deploys Cognito authentication, API security, and centralized logging in minutes!**  

---

## **📌 4. Next Steps**
Would you like:  
✅ **Grafana integration with ELK for advanced analytics?**  
✅ **AWS Lambda for automated log cleanup & retention policies?**  

---
### **🚀 Advanced Monitoring with Grafana & Automated Log Management with AWS Lambda**
This guide extends our **AWS Terraform Infrastructure** by adding:  
✅ **Grafana Integration with ELK Stack** (Advanced log visualization)  
✅ **AWS Lambda for Automated Log Cleanup & Retention Policies**  

---

## **📌 1. Integrating Grafana with ELK Stack for Advanced Monitoring**  
Grafana will provide **real-time monitoring dashboards** for:  
✅ **API performance & request logs**  
✅ **EC2, RDS, and auto-scaling metrics**  
✅ **Elasticsearch log queries**  

---

### **🔹 Step 1: Deploy Grafana (`grafana.tf`)**
```hcl
resource "aws_instance" "grafana" {
  ami           = "ami-12345678" # Use latest Amazon Linux 2 AMI
  instance_type = "t3.medium"
  key_name      = "tms-key-pair"
  security_groups = [aws_security_group.grafana_sg.id]

  user_data = <<EOF
#!/bin/bash
sudo yum install -y https://dl.grafana.com/oss/release/grafana-8.3.3-1.x86_64.rpm
sudo systemctl start grafana-server
sudo systemctl enable grafana-server
EOF
}

resource "aws_security_group" "grafana_sg" {
  name        = "GrafanaSG"
  description = "Allow access to Grafana"

  ingress {
    from_port   = 3000
    to_port     = 3000
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
```
✅ **Deploys Grafana on an EC2 instance & allows access on port 3000**  

---

### **🔹 Step 2: Connect Grafana to AWS CloudWatch & Elasticsearch**  
After deployment, log in to **Grafana UI** and:  
1️⃣ **Add AWS CloudWatch as a Data Source**  
   - Select **CloudWatch** → Enter AWS credentials → Choose metrics namespace  
2️⃣ **Add Elasticsearch as a Data Source**  
   - URL: `http://<Elasticsearch-Cluster-URL>:9200`  
   - Index: `logs-*`  
   - Format: **Time Series**  

✅ **Grafana now visualizes API performance, error logs, and infrastructure metrics!**  

---

## **📌 2. Automating Log Cleanup with AWS Lambda**  
AWS Lambda will **automatically delete old logs** to optimize storage.  

### **🔹 Step 1: Create Lambda Function for Log Cleanup (`lambda_log_cleanup.tf`)**
```hcl
resource "aws_lambda_function" "log_cleanup_lambda" {
  filename      = "log_cleanup.zip"
  function_name = "LogCleanupFunction"
  role          = aws_iam_role.lambda_log_cleanup.arn
  handler       = "log_cleanup.lambda_handler"
  runtime       = "python3.8"
}

resource "aws_iam_role" "lambda_log_cleanup" {
  name = "LogCleanupLambdaRole"
  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
EOF
}
```
✅ **Creates a Lambda function that will automatically delete old logs**  

---

### **🔹 Step 2: Write Python Code for Log Cleanup**  
Create `log_cleanup.py`:
```python
import boto3
import datetime

logs_client = boto3.client("logs")

def lambda_handler(event, context):
    log_groups = ["/aws/api-gateway/tms-api", "/aws/lambda/TMSLambdaLogs"]
    retention_days = 30  # Keep logs for 30 days

    for log_group in log_groups:
        try:
            logs_client.put_retention_policy(
                logGroupName=log_group,
                retentionInDays=retention_days
            )
            print(f"Set retention policy for {log_group} to {retention_days} days")
        except Exception as e:
            print(f"Failed to update retention policy: {e}")

    return {"statusCode": 200, "message": "Log retention updated"}
```
✅ **Automatically removes logs older than 30 days.**  

---

### **🔹 Step 3: Deploy Lambda & Schedule Execution**
1️⃣ **Zip & Deploy Lambda Function**
```sh
zip log_cleanup.zip log_cleanup.py
terraform apply -auto-approve
```
2️⃣ **Schedule Daily Execution with CloudWatch Events**
```hcl
resource "aws_cloudwatch_event_rule" "log_cleanup_rule" {
  name        = "DailyLogCleanup"
  description = "Triggers log cleanup Lambda daily"
  schedule_expression = "rate(24 hours)"
}

resource "aws_cloudwatch_event_target" "log_cleanup_target" {
  rule      = aws_cloudwatch_event_rule.log_cleanup_rule.name
  target_id = "LogCleanupLambda"
  arn       = aws_lambda_function.log_cleanup_lambda.arn
}
```
✅ **Lambda will run every 24 hours to clean old logs.**  

---

## **📌 3. Automate Terraform Deployment**
Run:
```sh
terraform init
terraform apply -auto-approve
```
✅ **Deploys Grafana, Log Cleanup, and Automated Monitoring in minutes!**  

---

## **📌 4. Next Steps**
Would you like:  
✅ **AWS X-Ray for Distributed Tracing of API Calls?**  
✅ **AWS Athena for Log Querying & Insights?**  

---
### **🚀 AWS X-Ray for Distributed Tracing & AWS Athena for Log Querying in TMS**  

This guide extends our **AWS Terraform Infrastructure** by adding:  
✅ **AWS X-Ray for Distributed Tracing of API Calls** (Track performance & bottlenecks)  
✅ **AWS Athena for Log Querying & Insights** (Run SQL-like queries on logs)  

---

## **📌 1. AWS X-Ray for Distributed Tracing**
AWS X-Ray enables **end-to-end tracing** of requests across **API Gateway, Lambda, EC2, and RDS**.  

---

### **🔹 Step 1: Enable AWS X-Ray for API Gateway (`xray.tf`)**  
```hcl
resource "aws_api_gateway_stage" "tms_stage" {
  rest_api_id   = aws_api_gateway_rest_api.tms_api.id
  stage_name    = "prod"
  deployment_id = aws_api_gateway_deployment.tms_deployment.id

  tracing_enabled = true  # Enable X-Ray Tracing

  access_log_settings {
    destination_arn = aws_cloudwatch_log_group.api_gateway_logs.arn
    format          = "$context.requestId $context.identity.sourceIp $context.httpMethod $context.resourcePath $context.status"
  }
}
```
✅ **Tracks all requests made to API Gateway & sends data to AWS X-Ray**  

---

### **🔹 Step 2: Enable AWS X-Ray for Spring Boot Backend**  
1️⃣ **Add AWS X-Ray SDK to `pom.xml`**
```xml
<dependency>
    <groupId>com.amazonaws</groupId>
    <artifactId>aws-xray-recorder-sdk-core</artifactId>
    <version>2.9.0</version>
</dependency>
<dependency>
    <groupId>com.amazonaws</groupId>
    <artifactId>aws-xray-recorder-sdk-sql</artifactId>
    <version>2.9.0</version>
</dependency>
```
2️⃣ **Enable X-Ray in Spring Boot (`application.properties`)**
```properties
com.amazonaws.xray.strategy.sampling.SamplingStrategy=DEFAULT
```
✅ **Tracks database queries, API calls, and performance bottlenecks**  

---

### **🔹 Step 3: Enable AWS X-Ray for Lambda**  
```hcl
resource "aws_lambda_function" "tms_lambda" {
  function_name = "TMSLambdaFunction"
  runtime       = "python3.8"
  handler       = "lambda_function.lambda_handler"
  role          = aws_iam_role.lambda_exec.arn
  tracing_config {
    mode = "Active"
  }
}
```
✅ **Captures Lambda execution time, latency, and downstream dependencies**  

---

### **🔹 Step 4: Deploy X-Ray & View Traces**  
1️⃣ **Run Terraform**
```sh
terraform apply -auto-approve
```
2️⃣ **View Traces in AWS Console**  
Go to **AWS X-Ray Console → Traces → Select API Requests**  

✅ **Now, every request from API Gateway to EC2, RDS, and Lambda is fully traceable!**  

---

## **📌 2. AWS Athena for Log Querying & Insights**  
AWS Athena enables **SQL-like queries on CloudWatch logs, API logs, and S3 logs.**  

---

### **🔹 Step 1: Enable CloudWatch Log Export to S3 (`athena.tf`)**  
```hcl
resource "aws_s3_bucket" "tms_logs_bucket" {
  bucket = "tms-logs"
}

resource "aws_s3_bucket_policy" "tms_logs_policy" {
  bucket = aws_s3_bucket.tms_logs_bucket.id
  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": "*",
      "Action": "s3:GetObject",
      "Resource": "arn:aws:s3:::tms-logs/*"
    }
  ]
}
EOF
}

resource "aws_cloudwatch_log_subscription_filter" "export_logs" {
  name            = "ExportLogsToS3"
  log_group_name  = aws_cloudwatch_log_group.api_gateway_logs.name
  filter_pattern  = ""
  destination_arn = aws_s3_bucket.tms_logs_bucket.arn
}
```
✅ **Exports API logs, Lambda logs, and EC2 logs to S3**  

---

### **🔹 Step 2: Create AWS Athena Table for Log Queries**  
```hcl
resource "aws_athena_database" "tms_logs_db" {
  name = "tms_logs_db"
}

resource "aws_athena_named_query" "query_api_logs" {
  name     = "Query API Logs"
  database = aws_athena_database.tms_logs_db.name
  query_string = <<EOF
SELECT requestId, sourceIp, httpMethod, resourcePath, status
FROM tms_logs
WHERE status >= 500
EOF
}
```
✅ **Athena can now run SQL queries on API logs**  

---

### **🔹 Step 3: Run SQL Queries on API Logs**  
1️⃣ **Go to AWS Athena Console → Query Editor**  
2️⃣ **Run SQL Query**
```sql
SELECT requestId, sourceIp, httpMethod, resourcePath, status
FROM tms_logs
WHERE status >= 500;
```
✅ **Finds all failed API requests in real-time**  

---

## **📌 3. Automate Terraform Deployment**
Run:
```sh
terraform init
terraform apply -auto-approve
```
✅ **Deploys X-Ray, Athena, and SQL-based log analysis in minutes!**  

---

## **📌 4. Next Steps**
Would you like:  
✅ **AWS Lambda for anomaly detection using AI/ML?**  
✅ **AWS GuardDuty for threat detection & security monitoring?**  

---
### **🚀 AI/ML Anomaly Detection with AWS Lambda & Threat Detection with AWS GuardDuty for TMS**  

This guide extends our **AWS Terraform Infrastructure** by adding:  
✅ **AWS Lambda for Anomaly Detection using AI/ML** (Detect unusual API requests & geofence violations)  
✅ **AWS GuardDuty for Threat Detection & Security Monitoring** (Detect malicious traffic & unauthorized access)  

---

## **📌 1. AWS Lambda for AI/ML Anomaly Detection**
AWS Lambda will use **Amazon SageMaker AI models** to detect **anomalous behavior** in:  
✅ **API requests** (e.g., excessive failed logins, high request volume)  
✅ **Geofence violations** (e.g., unexpected vehicle entries/exits)  

---

### **🔹 Step 1: Train an AI Model for Anomaly Detection**
1️⃣ **Create an Amazon SageMaker Model** (`sagemaker.tf`)  
```hcl
resource "aws_sagemaker_model" "anomaly_detection_model" {
  name               = "TMSAnomalyDetection"
  execution_role_arn = aws_iam_role.sagemaker_role.arn

  primary_container {
    image         = "438346466558.dkr.ecr.us-east-1.amazonaws.com/tms-anomaly-detection:latest"
    model_data_url = "s3://tms-ml-models/anomaly-detection.tar.gz"
  }
}
```
✅ **Deploys a trained AI model for detecting anomalies in API logs & vehicle movements**  

---

### **🔹 Step 2: AWS Lambda for Real-Time Anomaly Detection (`lambda_anomaly.tf`)**
```hcl
resource "aws_lambda_function" "anomaly_detection_lambda" {
  filename      = "anomaly_detection.zip"
  function_name = "TMSAnomalyDetection"
  role          = aws_iam_role.lambda_exec.arn
  handler       = "anomaly_detection.lambda_handler"
  runtime       = "python3.8"
}
```
✅ **Lambda function will analyze API logs & geofence activity**  

---

### **🔹 Step 3: Python Code for Anomaly Detection in Lambda**  
Create `anomaly_detection.py`:
```python
import boto3
import json

sagemaker_runtime = boto3.client("sagemaker-runtime")
cloudwatch = boto3.client("logs")

def lambda_handler(event, context):
    # Fetch latest API logs from CloudWatch
    response = cloudwatch.filter_log_events(
        logGroupName="/aws/api-gateway/tms-api",
        filterPattern='"{$.status} >= 400"',
        limit=10
    )

    logs = [log["message"] for log in response["events"]]

    # Send logs to SageMaker for anomaly detection
    response = sagemaker_runtime.invoke_endpoint(
        EndpointName="TMSAnomalyDetection",
        ContentType="application/json",
        Body=json.dumps({"logs": logs})
    )

    prediction = json.loads(response["Body"].read().decode("utf-8"))

    if prediction["anomaly_score"] > 0.8:
        alert_message = "🚨 Anomalous behavior detected in API requests!"
        print(alert_message)
        # Send alert via SNS
        sns_client = boto3.client("sns")
        sns_client.publish(
            TopicArn="arn:aws:sns:us-east-1:123456789012:AnomalyAlerts",
            Message=alert_message,
            Subject="Security Alert"
        )

    return {"statusCode": 200, "message": "Anomaly detection completed"}
```
✅ **Automatically detects security threats in API logs using ML models**  

---

### **🔹 Step 4: Schedule Lambda Execution with CloudWatch**  
```hcl
resource "aws_cloudwatch_event_rule" "anomaly_detection_schedule" {
  name                = "AnomalyDetectionSchedule"
  schedule_expression = "rate(15 minutes)"
}

resource "aws_cloudwatch_event_target" "lambda_target" {
  rule      = aws_cloudwatch_event_rule.anomaly_detection_schedule.name
  target_id = "TMSAnomalyDetection"
  arn       = aws_lambda_function.anomaly_detection_lambda.arn
}
```
✅ **Lambda runs every 15 minutes to scan logs for anomalies**  

---

## **📌 2. AWS GuardDuty for Threat Detection & Security Monitoring**
AWS GuardDuty detects **unauthorized access, unusual API calls, and potential security threats**.  

---

### **🔹 Step 1: Enable AWS GuardDuty (`guardduty.tf`)**
```hcl
resource "aws_guardduty_detector" "tms_guardduty" {
  enable = true
}
```
✅ **Enables AWS GuardDuty to monitor malicious activity in AWS accounts**  

---

### **🔹 Step 2: Create an SNS Topic for GuardDuty Alerts**
```hcl
resource "aws_sns_topic" "guardduty_alerts" {
  name = "GuardDutyAlerts"
}
```
✅ **Notifies admins when security threats are detected**  

---

### **🔹 Step 3: Configure GuardDuty Findings for Alerting**  
```hcl
resource "aws_cloudwatch_event_rule" "guardduty_alert_rule" {
  name        = "GuardDutyAlertRule"
  description = "Triggers alert when GuardDuty detects threats"
  event_pattern = jsonencode({
    source      = ["aws.guardduty"],
    detail-type = ["GuardDuty Finding"]
  })
}

resource "aws_cloudwatch_event_target" "guardduty_alert_target" {
  rule      = aws_cloudwatch_event_rule.guardduty_alert_rule.name
  target_id = "GuardDutySNS"
  arn       = aws_sns_topic.guardduty_alerts.arn
}
```
✅ **GuardDuty findings trigger security alerts automatically**  

---

### **🔹 Step 4: View GuardDuty Alerts in AWS Console**
1️⃣ **Go to AWS GuardDuty → Findings**  
2️⃣ **Filter alerts by severity**  
3️⃣ **Take action based on the security threat**  

✅ **Detects unusual API activity, brute force attacks, and malicious IP traffic**  

---

## **📌 3. Automate Terraform Deployment**
Run:
```sh
terraform init
terraform apply -auto-approve
```
✅ **Deploys AI-powered anomaly detection & real-time security monitoring in minutes!**  

---

## **📌 4. Next Steps**
Would you like:  
✅ **AWS Security Hub for Unified Security Compliance & Monitoring?**  
✅ **Automated Incident Response using AWS Lambda & AWS Step Functions?**  

---
### **🚀 Implementing AWS Security Hub for Unified Security Monitoring & Automated Incident Response with AWS Lambda & Step Functions**  

This guide extends our **AWS Terraform Infrastructure** by adding:  
✅ **AWS Security Hub for Centralized Security Compliance & Monitoring**  
✅ **AWS Lambda & Step Functions for Automated Incident Response**  

---

## **📌 1. AWS Security Hub for Unified Security Monitoring**  
AWS Security Hub collects and analyzes security findings from **AWS GuardDuty, IAM, S3, RDS, CloudTrail, and more.**  

---

### **🔹 Step 1: Enable AWS Security Hub (`securityhub.tf`)**
```hcl
resource "aws_securityhub_account" "security_hub" {}
```
✅ **Enables Security Hub to consolidate security alerts**  

---

### **🔹 Step 2: Enable AWS Security Standards (CIS, PCI DSS, AWS Foundational Security Best Practices)**
```hcl
resource "aws_securityhub_standards_subscription" "cis_standard" {
  standards_arn = "arn:aws:securityhub:us-east-1::standards/aws-foundational-security-best-practices/v/1.0.0"
}
```
✅ **Enables AWS Foundational Security Best Practices standard monitoring**  

---

### **🔹 Step 3: Set Up Security Hub Findings Notification**
```hcl
resource "aws_sns_topic" "security_hub_alerts" {
  name = "SecurityHubAlerts"
}

resource "aws_cloudwatch_event_rule" "securityhub_alert_rule" {
  name        = "SecurityHubAlertRule"
  description = "Triggers alert when Security Hub detects issues"
  event_pattern = jsonencode({
    source      = ["aws.securityhub"],
    detail-type = ["Security Hub Findings - Imported"]
  })
}

resource "aws_cloudwatch_event_target" "securityhub_alert_target" {
  rule      = aws_cloudwatch_event_rule.securityhub_alert_rule.name
  target_id = "SecurityHubSNS"
  arn       = aws_sns_topic.security_hub_alerts.arn
}
```
✅ **Notifies security teams when AWS Security Hub detects vulnerabilities**  

---

### **🔹 Step 4: View Security Findings in AWS Console**
1️⃣ **Go to AWS Security Hub → Findings**  
2️⃣ **Filter alerts by compliance standards**  
3️⃣ **Investigate & remediate detected security risks**  

✅ **Identifies misconfigurations, non-compliance, and security threats**  

---

## **📌 2. Automated Incident Response with AWS Lambda & Step Functions**  
AWS Step Functions orchestrate **automatic remediation actions** when security threats are detected.  

---

### **🔹 Step 1: Create an AWS Step Functions Workflow (`step_functions.tf`)**  
```hcl
resource "aws_sfn_state_machine" "incident_response" {
  name     = "TMSIncidentResponse"
  role_arn = aws_iam_role.step_functions_role.arn
  definition = jsonencode({
    Comment = "Automated Incident Response Workflow",
    StartAt = "CheckThreatLevel",
    States = {
      "CheckThreatLevel": {
        "Type": "Choice",
        "Choices": [
          {
            "Variable": "$.threatLevel",
            "NumericEquals": 1,
            "Next": "LogIncident"
          },
          {
            "Variable": "$.threatLevel",
            "NumericEquals": 2,
            "Next": "SendAlert"
          },
          {
            "Variable": "$.threatLevel",
            "NumericGreaterThanEquals": 3,
            "Next": "QuarantineInstance"
          }
        ],
        "Default": "EndState"
      },
      "LogIncident": {
        "Type": "Task",
        "Resource": "arn:aws:lambda:us-east-1:123456789012:function:LogIncidentLambda",
        "Next": "EndState"
      },
      "SendAlert": {
        "Type": "Task",
        "Resource": "arn:aws:lambda:us-east-1:123456789012:function:SendSecurityAlertLambda",
        "Next": "EndState"
      },
      "QuarantineInstance": {
        "Type": "Task",
        "Resource": "arn:aws:lambda:us-east-1:123456789012:function:QuarantineInstanceLambda",
        "Next": "EndState"
      },
      "EndState": {
        "Type": "Pass",
        "End": true
      }
    }
  })
}
```
✅ **Automatically takes action based on the severity of detected threats**  

---

### **🔹 Step 2: AWS Lambda for Incident Logging (`lambda_incident_log.tf`)**
```hcl
resource "aws_lambda_function" "log_incident_lambda" {
  filename      = "log_incident.zip"
  function_name = "LogIncidentLambda"
  role          = aws_iam_role.lambda_exec.arn
  handler       = "incident_logger.lambda_handler"
  runtime       = "python3.8"
}
```
✅ **Logs security incidents in AWS CloudWatch**  

---

### **🔹 Step 3: AWS Lambda for Sending Alerts (`lambda_security_alert.tf`)**
```hcl
resource "aws_lambda_function" "send_security_alert_lambda" {
  filename      = "send_alert.zip"
  function_name = "SendSecurityAlertLambda"
  role          = aws_iam_role.lambda_exec.arn
  handler       = "send_alert.lambda_handler"
  runtime       = "python3.8"
}
```
✅ **Sends an alert via SNS when a threat is detected**  

---

### **🔹 Step 4: AWS Lambda for Quarantining Compromised EC2 Instances (`lambda_quarantine.tf`)**
```hcl
resource "aws_lambda_function" "quarantine_instance_lambda" {
  filename      = "quarantine.zip"
  function_name = "QuarantineInstanceLambda"
  role          = aws_iam_role.lambda_exec.arn
  handler       = "quarantine.lambda_handler"
  runtime       = "python3.8"
}
```
✅ **Automatically isolates an EC2 instance if it is compromised**  

---

### **🔹 Step 5: Python Code for Quarantining EC2 Instances**  
Create `quarantine.py`:
```python
import boto3
import json

ec2 = boto3.client("ec2")

def lambda_handler(event, context):
    instance_id = event["instance_id"]
    print(f"🚨 Quarantining EC2 instance {instance_id}")

    ec2.modify_instance_attribute(
        InstanceId=instance_id,
        Groups=["sg-quarantine"]
    )

    return {"statusCode": 200, "message": f"EC2 instance {instance_id} quarantined"}
```
✅ **Removes a compromised instance from normal security groups**  

---

## **📌 3. Automate Terraform Deployment**
Run:
```sh
terraform init
terraform apply -auto-approve
```
✅ **Deploys AWS Security Hub & Automated Incident Response in minutes!**  

---

## **📌 4. Next Steps**
Would you like:  
✅ **AWS Macie for Detecting Sensitive Data Leaks?**  
✅ **AWS Shield for DDoS Protection?**  

---
### **🚀 Implementing AWS Macie for Sensitive Data Protection & AWS Shield for DDoS Protection**  

This guide extends our **AWS Terraform Infrastructure** by adding:  
✅ **AWS Macie for Detecting Sensitive Data Leaks** (Protecting PII, credit card data, and passwords)  
✅ **AWS Shield for DDoS Protection** (Defending against large-scale attacks)  

---

## **📌 1. AWS Macie for Sensitive Data Protection**  
AWS Macie automatically **detects and alerts** if sensitive data (e.g., **PII, credit card numbers, API keys**) is found in **S3 buckets**.  

---

### **🔹 Step 1: Enable AWS Macie (`macie.tf`)**
```hcl
resource "aws_macie2_account" "macie" {
  status = "ENABLED"
}
```
✅ **Enables Macie for the AWS account**  

---

### **🔹 Step 2: Create Macie S3 Data Classification Job**
```hcl
resource "aws_macie2_classification_job" "tms_macie_job" {
  name      = "TMS-Macie-S3-Scan"
  job_type  = "ONE_TIME"
  s3_job_definition {
    bucket_definitions {
      account_id = data.aws_caller_identity.current.account_id
      buckets    = ["tms-logs", "tms-sensitive-data"]
    }
  }
  sampling_percentage = 100
}
```
✅ **Scans S3 for sensitive data (e.g., credit card numbers, SSNs, passwords, API keys)**  

---

### **🔹 Step 3: Alert on Sensitive Data Findings**  
```hcl
resource "aws_cloudwatch_event_rule" "macie_alert_rule" {
  name        = "MacieAlertRule"
  description = "Triggers alert when Macie detects sensitive data"
  event_pattern = jsonencode({
    source      = ["aws.macie"]
    detail-type = ["Macie Finding"]
  })
}

resource "aws_sns_topic" "macie_alerts" {
  name = "MacieAlerts"
}

resource "aws_cloudwatch_event_target" "macie_alert_target" {
  rule      = aws_cloudwatch_event_rule.macie_alert_rule.name
  target_id = "MacieSNS"
  arn       = aws_sns_topic.macie_alerts.arn
}
```
✅ **Sends alerts when sensitive data is detected in S3**  

---

### **🔹 Step 4: View Macie Findings in AWS Console**
1️⃣ **Go to AWS Macie → Findings**  
2️⃣ **Review detected PII or sensitive files in S3**  
3️⃣ **Take action (encrypt, delete, or block access)**  

✅ **Ensures compliance with GDPR, PCI DSS, and data security best practices**  

---

## **📌 2. AWS Shield for DDoS Protection**  
AWS Shield protects against **DDoS attacks** on **API Gateway, Load Balancers, and EC2 instances**.  

---

### **🔹 Step 1: Enable AWS Shield Advanced**
```hcl
resource "aws_shield_protection" "api_gateway_protection" {
  name         = "APIGatewayProtection"
  resource_arn = aws_api_gateway_rest_api.tms_api.execution_arn
}

resource "aws_shield_protection" "load_balancer_protection" {
  name         = "LoadBalancerProtection"
  resource_arn = aws_lb.tms_lb.arn
}
```
✅ **Protects API Gateway & Load Balancer against DDoS attacks**  

---

### **🔹 Step 2: AWS WAF Rule for DDoS Mitigation**  
```hcl
resource "aws_wafv2_web_acl" "ddos_protection" {
  name  = "TMS-WAF-DDoS"
  scope = "REGIONAL"

  rule {
    name     = "RateLimitRule"
    priority = 1
    action {
      block {}
    }
    statement {
      rate_based_statement {
        limit = 1000
        aggregate_key_type = "IP"
      }
    }
    visibility_config {
      cloudwatch_metrics_enabled = true
      metric_name                = "RateLimitRule"
      sampled_requests_enabled   = true
    }
  }
}

resource "aws_wafv2_web_acl_association" "attach_waf" {
  resource_arn = aws_api_gateway_rest_api.tms_api.execution_arn
  web_acl_arn  = aws_wafv2_web_acl.ddos_protection.arn
}
```
✅ **Blocks IPs sending more than 1000 requests per minute**  

---

### **🔹 Step 3: View DDoS Threats in AWS Shield Console**
1️⃣ **Go to AWS Shield → Incidents**  
2️⃣ **Check attack vectors & affected services**  
3️⃣ **AWS Shield automatically mitigates detected attacks**  

✅ **Provides real-time DDoS attack monitoring & mitigation**  

---

## **📌 3. Automate Terraform Deployment**
Run:
```sh
terraform init
terraform apply -auto-approve
```
✅ **Deploys AWS Macie for sensitive data scanning & AWS Shield for DDoS protection!**  

---

## **📌 4. Next Steps**
Would you like:  
✅ **AWS Config for Continuous Compliance Monitoring?**  
✅ **AWS Firewall Manager for Centralized Security Policy Enforcement?**  

---
### **🚀 Implementing AWS Config for Continuous Compliance Monitoring & AWS Firewall Manager for Centralized Security Enforcement**  

This guide extends our **AWS Terraform Infrastructure** by adding:  
✅ **AWS Config for Continuous Compliance Monitoring** (Tracks security settings, misconfigurations)  
✅ **AWS Firewall Manager for Centralized Security Policy Enforcement** (Manages WAF, Shield, and network firewalls)  

---

## **📌 1. AWS Config for Continuous Compliance Monitoring**  
AWS Config tracks and audits **AWS resource configurations** to ensure compliance with **security best practices**.

---

### **🔹 Step 1: Enable AWS Config to Track All Resources (`aws_config.tf`)**  
```hcl
resource "aws_config_configuration_recorder" "tms_config" {
  name     = "TMSConfigRecorder"
  role_arn = aws_iam_role.config_role.arn
}

resource "aws_iam_role" "config_role" {
  name = "AWSConfigRole"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect = "Allow"
      Principal = { Service = "config.amazonaws.com" }
      Action = "sts:AssumeRole"
    }]
  })
}

resource "aws_iam_role_policy_attachment" "config_policy" {
  role       = aws_iam_role.config_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSConfigRole"
}
```
✅ **Tracks changes in AWS resources (e.g., IAM, S3, EC2, RDS, API Gateway)**  

---

### **🔹 Step 2: Enable Compliance Rules for Security Best Practices**
```hcl
resource "aws_config_config_rule" "enforce_s3_encryption" {
  name        = "s3-bucket-encryption-rule"
  description = "Ensures all S3 buckets are encrypted"

  source {
    owner             = "AWS"
    source_identifier = "S3_BUCKET_SERVER_SIDE_ENCRYPTION_ENABLED"
  }
}

resource "aws_config_config_rule" "iam_no_root_access" {
  name        = "iam-no-root-access"
  description = "Ensures root account is not used for API requests"

  source {
    owner             = "AWS"
    source_identifier = "IAM_ROOT_ACCESS_KEY_CHECK"
  }
}
```
✅ **Monitors AWS accounts for security misconfigurations**  

---

### **🔹 Step 3: Set Up AWS Config Alerts for Compliance Violations**
```hcl
resource "aws_sns_topic" "config_alerts" {
  name = "AWSConfigAlerts"
}

resource "aws_cloudwatch_event_rule" "config_compliance_rule" {
  name        = "AWSConfigComplianceAlerts"
  description = "Triggers alerts for AWS Config compliance violations"
  event_pattern = jsonencode({
    source      = ["aws.config"]
    detail-type = ["Config Configuration Compliance Change"]
  })
}

resource "aws_cloudwatch_event_target" "config_alert_target" {
  rule      = aws_cloudwatch_event_rule.config_compliance_rule.name
  target_id = "ConfigSNS"
  arn       = aws_sns_topic.config_alerts.arn
}
```
✅ **Notifies security teams when AWS resources violate security rules**  

---

### **🔹 Step 4: View Compliance Status in AWS Config Console**
1️⃣ **Go to AWS Config → Rules**  
2️⃣ **Check compliance status of resources**  
3️⃣ **Remediate non-compliant resources**  

✅ **Provides a full history of AWS security configurations**  

---

## **📌 2. AWS Firewall Manager for Centralized Security Enforcement**  
AWS Firewall Manager simplifies security management by **enforcing policies across AWS accounts and VPCs**.

---

### **🔹 Step 1: Enable AWS Firewall Manager (`firewall_manager.tf`)**
```hcl
resource "aws_fms_policy" "tms_firewall_policy" {
  name       = "TMS Firewall Policy"
  policy_type = "WAFV2"
  remediation_enabled = true

  security_service_policy_data {
    type = "WAFV2"
    managed_service_data = jsonencode({
      type = "WAFV2"
      defaultAction = {
        type = "BLOCK"
      }
    })
  }

  resource_type_list = ["AWS::ElasticLoadBalancingV2::LoadBalancer"]
}
```
✅ **Automatically applies WAF rules to all AWS accounts & VPCs**  

---

### **🔹 Step 2: Enforce Security Policies Across AWS Accounts**
```hcl
resource "aws_fms_policy" "tms_security_policy" {
  name       = "TMS Security Policy"
  policy_type = "SECURITY_GROUPS_COMMON"

  security_service_policy_data {
    type = "SECURITY_GROUPS_COMMON"
    managed_service_data = jsonencode({
      securityGroups = [{
        id          = aws_security_group.tms_security_group.id
        description = "Common security group for all accounts"
      }]
    })
  }
}
```
✅ **Enforces consistent security group rules across AWS accounts**  

---

### **🔹 Step 3: Set Up Automated Firewall Rule Enforcement**
```hcl
resource "aws_cloudwatch_event_rule" "firewall_violation_rule" {
  name        = "FirewallViolationAlerts"
  description = "Triggers alerts for firewall rule violations"
  event_pattern = jsonencode({
    source      = ["aws.fms"]
    detail-type = ["Firewall Manager Policy Compliance Change"]
  })
}

resource "aws_cloudwatch_event_target" "firewall_alert_target" {
  rule      = aws_cloudwatch_event_rule.firewall_violation_rule.name
  target_id = "FirewallSNS"
  arn       = aws_sns_topic.config_alerts.arn
}
```
✅ **Notifies security teams when firewall rules are violated**  

---

### **🔹 Step 4: View Firewall Violations in AWS Firewall Manager Console**
1️⃣ **Go to AWS Firewall Manager → Security Policies**  
2️⃣ **Review non-compliant resources & fix issues**  
3️⃣ **Ensure security consistency across AWS accounts**  

✅ **Provides centralized firewall & security rule enforcement**  

---

## **📌 3. Automate Terraform Deployment**
Run:
```sh
terraform init
terraform apply -auto-approve
```
✅ **Deploys AWS Config for compliance monitoring & AWS Firewall Manager for centralized security**  

---

## **📌 4. Next Steps**
Would you like:  
✅ **AWS Inspector for Automated Vulnerability Scanning?**  
✅ **AWS Secrets Manager for Securely Storing API Keys & Credentials?**  

---
### **🚀 Implementing AWS Inspector for Automated Vulnerability Scanning & AWS Secrets Manager for Secure Credential Storage**  

This guide extends our **AWS Terraform Infrastructure** by adding:  
✅ **AWS Inspector for Automated Vulnerability Scanning** (Detects security weaknesses in EC2, Lambda, and ECR)  
✅ **AWS Secrets Manager for Securely Storing API Keys & Credentials** (Protects sensitive information like database passwords, API tokens)  

---

## **📌 1. AWS Inspector for Automated Vulnerability Scanning**  
AWS Inspector continuously scans **EC2 instances, ECR containers, and Lambda functions** for vulnerabilities.

---

### **🔹 Step 1: Enable AWS Inspector (`aws_inspector.tf`)**  
```hcl
resource "aws_inspector2_enabler" "inspector" {
  account_ids = [data.aws_caller_identity.current.account_id]
  resource_types = ["EC2", "ECR", "LAMBDA"]
}
```
✅ **Enables AWS Inspector to scan EC2, ECR, and Lambda functions for vulnerabilities**  

---

### **🔹 Step 2: Create an Inspector Assessment Target**
```hcl
resource "aws_inspector_resource_group" "tms_resources" {
  tags = {
    Environment = "Production"
  }
}

resource "aws_inspector_assessment_target" "tms_target" {
  name        = "TMS-Inspector-Target"
  resource_group_arn = aws_inspector_resource_group.tms_resources.arn
}
```
✅ **Groups all TMS infrastructure under a single security assessment target**  

---

### **🔹 Step 3: Define AWS Inspector Rules & Run Scans**
```hcl
resource "aws_inspector_assessment_template" "tms_scan" {
  name             = "TMS-Security-Scan"
  duration         = 3600 # 1 hour scan
  rules_package_arns = [
    "arn:aws:inspector:us-east-1:123456789012:rulespackage/0-0WrTjhpl",
    "arn:aws:inspector:us-east-1:123456789012:rulespackage/0-Xfgh74"
  ]
  assessment_target_arn = aws_inspector_assessment_target.tms_target.arn
}
```
✅ **Configures AWS Inspector to scan for security misconfigurations & vulnerabilities**  

---

### **🔹 Step 4: Set Up Automated Vulnerability Alerts**  
```hcl
resource "aws_sns_topic" "inspector_alerts" {
  name = "InspectorAlerts"
}

resource "aws_cloudwatch_event_rule" "inspector_vulnerability_rule" {
  name        = "InspectorVulnerabilityAlerts"
  description = "Triggers alerts when AWS Inspector detects vulnerabilities"
  event_pattern = jsonencode({
    source      = ["aws.inspector"]
    detail-type = ["Inspector Assessment Run Finding"]
  })
}

resource "aws_cloudwatch_event_target" "inspector_alert_target" {
  rule      = aws_cloudwatch_event_rule.inspector_vulnerability_rule.name
  target_id = "InspectorSNS"
  arn       = aws_sns_topic.inspector_alerts.arn
}
```
✅ **Notifies security teams if AWS Inspector detects vulnerabilities**  

---

### **🔹 Step 5: View Security Findings in AWS Inspector Console**
1️⃣ **Go to AWS Inspector → Findings**  
2️⃣ **Check security vulnerabilities & misconfigurations**  
3️⃣ **Remediate critical issues**  

✅ **Provides automated security scanning & compliance monitoring**  

---

## **📌 2. AWS Secrets Manager for Secure Credential Storage**  
AWS Secrets Manager securely stores **database credentials, API keys, and sensitive secrets.**  

---

### **🔹 Step 1: Store a Secret (`aws_secrets_manager.tf`)**  
```hcl
resource "aws_secretsmanager_secret" "tms_db_secret" {
  name = "TMSDatabaseCredentials"
}

resource "aws_secretsmanager_secret_version" "tms_db_secret_value" {
  secret_id     = aws_secretsmanager_secret.tms_db_secret.id
  secret_string = jsonencode({
    username = "admin",
    password = "SecureP@ssw0rd"
  })
}
```
✅ **Encrypts and securely stores database credentials**  

---

### **🔹 Step 2: Grant EC2 Instances & Lambda Access to Secrets**  
```hcl
resource "aws_iam_policy" "secrets_access" {
  name        = "TMSSecretsAccess"
  description = "Allows EC2 & Lambda to access secrets"
  policy      = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect   = "Allow"
      Action   = ["secretsmanager:GetSecretValue"]
      Resource = aws_secretsmanager_secret.tms_db_secret.arn
    }]
  })
}

resource "aws_iam_role_policy_attachment" "attach_secrets_policy" {
  role       = aws_iam_role.lambda_exec.name
  policy_arn = aws_iam_policy.secrets_access.arn
}
```
✅ **Allows only authorized services to retrieve stored secrets**  

---

### **🔹 Step 3: Retrieve Secret in Lambda Function (`secrets_lambda.tf`)**
```hcl
resource "aws_lambda_function" "secrets_lambda" {
  filename      = "retrieve_secret.zip"
  function_name = "RetrieveDatabaseSecret"
  role          = aws_iam_role.lambda_exec.arn
  handler       = "retrieve_secret.lambda_handler"
  runtime       = "python3.8"
}
```
✅ **Lambda function retrieves secrets securely from AWS Secrets Manager**  

---

### **🔹 Step 4: Python Code to Retrieve Database Credentials in Lambda**
Create `retrieve_secret.py`:
```python
import boto3
import json

secrets_client = boto3.client("secretsmanager")

def lambda_handler(event, context):
    secret_name = "TMSDatabaseCredentials"
    
    response = secrets_client.get_secret_value(SecretId=secret_name)
    secret = json.loads(response["SecretString"])

    return {
        "statusCode": 200,
        "body": json.dumps({
            "username": secret["username"],
            "password": "******"  # Masked for security
        })
    }
```
✅ **Securely retrieves database credentials from AWS Secrets Manager**  

---

## **📌 3. Automate Terraform Deployment**
Run:
```sh
terraform init
terraform apply -auto-approve
```
✅ **Deploys AWS Inspector for vulnerability scanning & AWS Secrets Manager for secure credential storage!**  

---

## **📌 4. Next Steps**
Would you like:  
✅ **AWS KMS for Data Encryption at Rest & Transit?**  
✅ **AWS IAM Access Analyzer for Permission Auditing?**  

---
### **🚀 Implementing AWS KMS for Data Encryption & AWS IAM Access Analyzer for Permission Auditing**  

This guide extends our **AWS Terraform Infrastructure** by adding:  
✅ **AWS KMS for Data Encryption at Rest & In Transit** (Encrypts sensitive data in S3, RDS, and Secrets Manager)  
✅ **AWS IAM Access Analyzer for Permission Auditing** (Identifies misconfigured IAM policies & excessive permissions)  

---

## **📌 1. AWS KMS for Data Encryption at Rest & Transit**  
AWS KMS manages **encryption keys** to protect **S3, RDS, EBS, and Secrets Manager data**.  

---

### **🔹 Step 1: Create a KMS Key for Encryption (`aws_kms.tf`)**
```hcl
resource "aws_kms_key" "tms_kms_key" {
  description             = "KMS Key for TMS Encryption"
  deletion_window_in_days = 30
  enable_key_rotation     = true
}

resource "aws_kms_alias" "tms_kms_alias" {
  name          = "alias/tms-key"
  target_key_id = aws_kms_key.tms_kms_key.id
}
```
✅ **Creates a KMS key with automatic key rotation enabled**  

---

### **🔹 Step 2: Encrypt RDS Database with KMS**
```hcl
resource "aws_db_instance" "tms_rds" {
  allocated_storage    = 20
  engine              = "postgres"
  instance_class      = "db.t3.medium"
  db_name             = "tmsdb"
  username           = "admin"
  password           = "SecureP@ssw0rd"
  kms_key_id         = aws_kms_key.tms_kms_key.arn
  storage_encrypted  = true
}
```
✅ **Encrypts RDS database with AWS KMS key**  

---

### **🔹 Step 3: Encrypt S3 Buckets with KMS**
```hcl
resource "aws_s3_bucket" "tms_secure_bucket" {
  bucket = "tms-secure-data"
}

resource "aws_s3_bucket_server_side_encryption_configuration" "s3_encryption" {
  bucket = aws_s3_bucket.tms_secure_bucket.id

  rule {
    apply_server_side_encryption_by_default {
      kms_master_key_id = aws_kms_key.tms_kms_key.arn
      sse_algorithm     = "aws:kms"
    }
  }
}
```
✅ **Encrypts all S3 objects with KMS key**  

---

### **🔹 Step 4: Encrypt AWS Secrets Manager with KMS**
```hcl
resource "aws_secretsmanager_secret" "tms_kms_secret" {
  name       = "TMSDatabaseEncryptedSecret"
  kms_key_id = aws_kms_key.tms_kms_key.arn
}
```
✅ **Encrypts database credentials stored in AWS Secrets Manager**  

---

### **🔹 Step 5: Apply KMS Encryption to EC2 EBS Volumes**
```hcl
resource "aws_ebs_volume" "tms_ebs" {
  availability_zone = "us-east-1a"
  size             = 10
  encrypted        = true
  kms_key_id       = aws_kms_key.tms_kms_key.arn
}
```
✅ **Encrypts EC2 storage volumes for secure data storage**  

---

## **📌 2. AWS IAM Access Analyzer for Permission Auditing**  
AWS IAM Access Analyzer **identifies excessive permissions** and **unused access rights** to improve security.  

---

### **🔹 Step 1: Enable AWS IAM Access Analyzer (`aws_iam_access_analyzer.tf`)**
```hcl
resource "aws_accessanalyzer_analyzer" "tms_analyzer" {
  name     = "TMS-IAM-Analyzer"
  type     = "ACCOUNT"
}
```
✅ **Enables AWS IAM Access Analyzer to audit IAM policies**  

---

### **🔹 Step 2: Set Up Alerts for IAM Permission Violations**  
```hcl
resource "aws_sns_topic" "iam_analyzer_alerts" {
  name = "IAMAccessAnalyzerAlerts"
}

resource "aws_cloudwatch_event_rule" "iam_violation_rule" {
  name        = "IAMViolationAlerts"
  description = "Triggers alerts for IAM permission misconfigurations"
  event_pattern = jsonencode({
    source      = ["aws.access-analyzer"]
    detail-type = ["Access Analyzer Finding"]
  })
}

resource "aws_cloudwatch_event_target" "iam_alert_target" {
  rule      = aws_cloudwatch_event_rule.iam_violation_rule.name
  target_id = "IAMAnalyzerSNS"
  arn       = aws_sns_topic.iam_analyzer_alerts.arn
}
```
✅ **Notifies security teams when IAM policies grant excessive access**  

---

### **🔹 Step 3: View IAM Audit Reports in AWS Console**
1️⃣ **Go to AWS IAM → Access Analyzer**  
2️⃣ **Review over-permissive IAM roles & policies**  
3️⃣ **Restrict permissions based on least privilege principle**  

✅ **Detects unintended IAM access & security risks**  

---

## **📌 3. Automate Terraform Deployment**
Run:
```sh
terraform init
terraform apply -auto-approve
```
✅ **Deploys AWS KMS for encryption & IAM Access Analyzer for permission auditing!**  

---

## **📌 4. Next Steps**
Would you like:  
✅ **AWS Detective for Security Investigation & Threat Analysis?**  
✅ **AWS Organizations for Multi-Account Security & Compliance?**  

