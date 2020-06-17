# efiling-worker

### Configuration

You should use environment variables to configure the efiling worker

| Environment Variable | Type    | Description                                  | Notes                          |
| -------------------- | ------- | -------------------------------------------- | ------------------------------ |
| SERVER_PORT          | Integer | web application server port                  | defaulted to `8080`            |
| RABBITMQ_HOST        | String  | RabbitMQ host                                | defaulted to `localhost`       |
| RABBITMQ_PORT        | Integer | RabbitMQ port                                | defaulted to `5672`            |
| RABBITMQ_PASSWORD    | String  | RabbitMQ password                            | defaulted to `guest`           |
| RABBITMQ_USERNAME    | String  | RabbitMQ Username                            | defaulted to `guest`           |


## Backend Folder Structure

The backend API will follow the standard Java Spring Boot MVC model for folder structure breakdown where there are `models` and `controllers`.
