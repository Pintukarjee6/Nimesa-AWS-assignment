2. The application will start running on `http://localhost:8080`.

## API Endpoints
The application provides the following API endpoints:

- `POST /api/discoverServices`: Discovers EC2 instances and S3 buckets and stores the information in the database.
- `GET /api/getJobResult/{jobId}`: Retrieves the status of a discovery job.
- `GET /api/getDiscoveryResult/{service}`: Retrieves the discovered resources for a specific service (EC2 or S3).
- `POST /api/getS3BucketObjects`: Discovers the objects in a specific S3 bucket and stores the information in the database.
- `GET /api/getS3BucketObjectCount`: Retrieves the number of objects in a specific S3 bucket.
- `GET /api/getS3BucketObjectlike`: Retrieves the objects in a specific S3 bucket that match a given pattern.

## Configuration
The application uses the following configuration properties:

- `spring.datasource.url`: The URL of the MySQL database.
- `spring.datasource.username`: The username for the MySQL database.
- `spring.datasource.password`: The password for the MySQL database.
- `aws.accessKeyId`: The AWS access key ID.
- `aws.secretKey`: The AWS secret key.
- `aws.static.region`: The AWS region to use.
