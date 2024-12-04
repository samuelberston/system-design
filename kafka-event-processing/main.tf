# Configure AWS Provider
provider "aws" {
  region = "us-west-2"
}

# VPC and Networking configuration (simplified)
resource "aws_vpc" "kafka_vpc" {
  cidr_block = "10.0.0.0/16"
  
  tags = {
    Name = "kafka-vpc"
  }
}

# Security Group for Kafka
resource "aws_security_group" "kafka" {
  name        = "kafka-security-group"
  description = "Security group for Kafka cluster"
  vpc_id      = aws_vpc.kafka_vpc.id

  # Kafka broker port
  ingress {
    from_port   = 9092
    to_port     = 9092
    protocol    = "tcp"
    cidr_blocks = ["10.0.0.0/16"]
  }

  # Zookeeper port
  ingress {
    from_port   = 2181
    to_port     = 2181
    protocol    = "tcp"
    cidr_blocks = ["10.0.0.0/16"]
  }
}

# Launch Template for Kafka brokers
resource "aws_launch_template" "kafka_broker" {
  name_prefix   = "kafka-broker"
  image_id      = "ami-038bba9a164eb3dc1"
  instance_type = "t3.large"

  user_data = base64encode(<<-EOF
              #!/bin/bash
              # Install and configure Kafka
              sudo yum install java-1.8.0
              wget https://archive.apache.org/dist/kafka/2.2.1/kafka_2.12-2.2.1.tgz
              tar -xzf kafka_2.12-2.2.1.tgz

              # Set broker.id dynamically
              # Configure other Kafka settings
              EOF
  )

  vpc_security_group_ids = [aws_security_group.kafka.id]

  iam_instance_profile {
    name = aws_iam_instance_profile.kafka_profile.name
  }
}

# Auto Scaling Group for Kafka brokers
resource "aws_autoscaling_group" "kafka_cluster" {
  name                = "kafka-cluster"
  desired_capacity    = 3
  max_size            = 5
  min_size            = 3
  target_group_arns   = [aws_lb_target_group.kafka.arn]
  vpc_zone_identifier = aws_subnet.private[*].id

  launch_template {
    id      = aws_launch_template.kafka_broker.id
    version = "$Latest"
  }

  tag {
    key                 = "Name"
    value               = "kafka-broker"
    propagate_at_launch = true
  }
}

# Network Load Balancer for Kafka
resource "aws_lb" "kafka" {
  name               = "kafka-nlb"
  internal           = true
  load_balancer_type = "network"
  subnets            = aws_subnet.private[*].id
}

resource "aws_lb_target_group" "kafka" {
  name     = "kafka-tg"
  port     = 9092
  protocol = "TCP"
  vpc_id   = aws_vpc.kafka_vpc.id
}

resource "aws_lb_listener" "kafka" {
  load_balancer_arn = aws_lb.kafka.arn
  port              = 9092
  protocol          = "TCP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.kafka.arn
  }
}

# Launch Template for ZooKeeper nodes
resource "aws_launch_template" "zookeeper_node" {
  name_prefix   = "zookeeper-node"
  image_id      = "ami-12345678" # Replace with appropriate AMI
  instance_type = "t3.medium"

  user_data = base64encode(<<-EOF
              #!/bin/bash
              # Install and configure ZooKeeper
              # Set myid dynamically
              # Configure other ZooKeeper settings
              EOF
  )

  vpc_security_group_ids = [aws_security_group.kafka.id]

  iam_instance_profile {
    name = aws_iam_instance_profile.kafka_profile.name
  }
}

# Auto Scaling Group for ZooKeeper nodes
resource "aws_autoscaling_group" "zookeeper_cluster" {
  name                = "zookeeper-cluster"
  desired_capacity    = 3
  max_size            = 3
  min_size            = 3
  vpc_zone_identifier = aws_subnet.private[*].id

  launch_template {
    id      = aws_launch_template.zookeeper_node.id
    version = "$Latest"
  }

  tag {
    key                 = "Name"
    value               = "zookeeper-node"
    propagate_at_launch = true
  }
}

