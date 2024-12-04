# IAM role for Kafka instances
resource "aws_iam_role" "kafka_role" {
  name = "kafka-instance-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      }
    ]
  })
}

# IAM instance profile
resource "aws_iam_instance_profile" "kafka_profile" {
  name = "kafka-instance-profile"
  role = aws_iam_role.kafka_role.name
}

# IAM policy for Kafka instances
resource "aws_iam_role_policy" "kafka_policy" {
  name = "kafka-instance-policy"
  role = aws_iam_role.kafka_role.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "ec2:DescribeInstances",
          "ec2:DescribeTags",
          "autoscaling:DescribeAutoScalingGroups"
        ]
        Resource = "*"
      }
    ]
  })
}