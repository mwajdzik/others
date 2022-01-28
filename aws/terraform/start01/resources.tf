resource "aws_vpc" "vpc1" {
  cidr_block           = var.vpc_cidr
  enable_dns_hostnames = "true"
}

resource "aws_subnet" "subnet1" {
  cidr_block              = var.subnet1_cidr
  vpc_id                  = aws_vpc.vpc1.id
  map_public_ip_on_launch = "true"
  availability_zone       = data.aws_availability_zones.available.names[1]
}

resource "aws_internet_gateway" "gateway1" {
  vpc_id = aws_vpc.vpc1.id
}

resource "aws_route_table" "route_table1" {
  vpc_id = aws_vpc.vpc1.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.gateway1.id
  }
}

resource "aws_route_table_association" "route-subnet1" {
  subnet_id      = aws_subnet.subnet1.id
  route_table_id = aws_route_table.route_table1.id
}

resource "aws_security_group" "sg-nodejs-instance" {
  name   = "nodejs_sg"
  vpc_id = aws_vpc.vpc1.id

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "nodejs1" {
  key_name      = var.ssh_key_name
  subnet_id     = aws_subnet.subnet1.id
  ami           = data.aws_ami.aws-linux.id
  instance_type = var.environment_instance_type.DEV
  monitoring    = var.environment_instance_settings.DEV.monitoring

  vpc_security_group_ids = [
    aws_security_group.sg-nodejs-instance.id
  ]

  tags = {
    Environment = var.environment_instance_type.DEV
  }

  connection {
    type        = "ssh"
    host        = self.public_ip
    user        = "ec2-user"
    private_key = file(var.private_key_path)
  }
}
