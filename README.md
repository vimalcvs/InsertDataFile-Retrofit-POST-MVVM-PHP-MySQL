# Insert Data with Retrofit and MVVM

This project demonstrates how to insert form data, including an image, name, and integer data, using Retrofit with MVVM architecture and Coroutines. The data is posted to a PHP API using Retrofit's `POST` method with `MultipartBody.Part`, and the image is saved in a server folder with the information stored in a MySQL database.

## Features

- Insert form data (image, name, and integer) using Retrofit.
- Use MVVM architecture for clean and maintainable code.
- Handle asynchronous operations with Coroutines.
- Save image on the server and store information in a MySQL database.

## Technologies Used

- **Retrofit**: For network operations.
- **MVVM**: Model-View-ViewModel architecture.
- **Coroutines**: For asynchronous operations.
- **PHP**: Backend API to handle data insertion.
- **MySQL**: Database to store information.
- **MultipartBody.Part**: To handle file uploads.

## Requirements

- Android Studio
- PHP Server (e.g., XAMPP, WAMP)
- MySQL Database

## Getting Started

### Setup PHP Server

1. Install a PHP server like XAMPP or WAMP.
2. Create a MySQL database and a table to store the form data.
3. Place the PHP API script in the server directory.
4. Update the PHP script with your database credentials.

### Clone the Repository

```bash
git clone https://github.com/vimalcvs/Retrofit-POST-MVVM-Form-Image-Upload.git
cd Retrofit-POST-MVVM-Form-Image-Upload
```

### Configure Android Project

1. Open the project in Android Studio.
2. Update the `BASE_URL` in your Retrofit service to point to your PHP server.
3. Build and run the project on your Android device or emulator.

## Usage

1. Fill in the form with an image, string, and integer data.
2. Submit the form to send a POST request to the PHP API.
3. Check your server folder for the uploaded image and the MySQL database for the inserted data.

## Code Structure

- **ApiService.kt**: Defines the Retrofit API endpoints.
- **Repository.kt**: Handles data operations and acts as a mediator between ViewModel and ApiService.
- **ViewModel.kt**: Manages UI-related data and handles form submission logic.
- **MainActivity.kt**: UI code to collect form data and interact with ViewModel.

## API Endpoint

Ensure your PHP API endpoint handles multipart form data. Example PHP script:

```php
<?php
$target_dir = "uploads/";
$target_file = $target_dir . basename($_FILES["file"]["name"]);
$imageFileType = strtolower(pathinfo($target_file, PATHINFO_EXTENSION));

// Check if image file is a actual image or fake image
if(isset($_POST["submit"])) {
    $check = getimagesize($_FILES["file"]["tmp_name"]);
    if($check !== false) {
        if (move_uploaded_file($_FILES["file"]["tmp_name"], $target_file)) {
            // Insert data into MySQL
            $servername = "localhost";
            $username = "username";
            $password = "password";
            $dbname = "database";

            $conn = new mysqli($servername, $username, $password, $dbname);
            if ($conn->connect_error) {
                die("Connection failed: " . $conn->connect_error);
            }

            $name = $_POST['name'];
            $integer = $_POST['integer'];
            $image = basename($_FILES["file"]["name"]);

            $sql = "INSERT INTO your_table (name, integer, image) VALUES ('$name', '$integer', '$image')";
            if ($conn->query($sql) === TRUE) {
                echo "New record created successfully";
            } else {
                echo "Error: " . $sql . "<br>" . $conn->error;
            }

            $conn->close();
        } else {
            echo "Sorry, there was an error uploading your file.";
        }
    } else {
        echo "File is not an image.";
    }
}
?>
```

## Contributing

Contributions are welcome! Please fork this repository and submit a pull request for any changes.
