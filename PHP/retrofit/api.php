<?php

require_once("config.php");

class InsertFood {
    
    function startInsertFood() {
        $connection = new Connection();
        $conn = $connection->getConnection();

        // Array for JSON response
        $response = array();

        $foodName = $_POST['foodname'];
        $foodQty = $_POST['foodqty'];
        $imageFile = $_FILES['foodimage']['name'];
        $imageTmpName = $_FILES['foodimage']['tmp_name'];
        $uploadDir = 'uploads/';

        // Allowed image file extensions
        $allowedExtensions = ['jpg', 'jpeg', 'png', 'gif'];

        try {
            if (isset($foodName) && isset($foodQty) && isset($imageFile)) {
                // Check if the food already exists
                $sqlCheck = "SELECT * FROM food WHERE foodname = :foodname";
                $stmtCheck = $conn->prepare($sqlCheck);
                $stmtCheck->bindParam(':foodname', $foodName);
                $stmtCheck->execute();

                if ($stmtCheck->rowCount() > 0) {
                    // Food already exists
                    $response["success"] = 0;
                    $response["message"] = "Food already exists!";
                } else {
                    // Validate the image file format
                    $fileExtension = strtolower(pathinfo($imageFile, PATHINFO_EXTENSION));
                    if (!in_array($fileExtension, $allowedExtensions)) {
                        $response["success"] = 0;
                        $response["message"] = "Invalid file format! Only JPG, JPEG, PNG, and GIF are allowed.";
                    } else {
                        // Generate a unique name for the image file
                        $uniqueImageName = uniqid('food_', true) . '.' . $fileExtension;
                        $imagePath = $uploadDir . $uniqueImageName;

                        // Move the uploaded image to the designated folder
                        if (move_uploaded_file($imageTmpName, $imagePath)) {
                            // Insert new food
                            $sqlInsert = "INSERT INTO food (foodname, foodqty, foodimage) VALUES (:foodname, :foodqty, :foodimage)";
                            $stmtInsert = $conn->prepare($sqlInsert);
                            $stmtInsert->bindParam(':foodname', $foodName);
                            $stmtInsert->bindParam(':foodqty', $foodQty);
                            $stmtInsert->bindParam(':foodimage', $imagePath);
                            $stmtInsert->execute();

                            // Check if the row was inserted
                            if ($stmtInsert->rowCount() > 0) {
                                // Success inserted
                                $response["success"] = 1;
                                $response["message"] = "Food successfully inserted!";
                            } else {
                                // Failed inserted
                                $response["success"] = 0;
                                $response["message"] = "Failed to insert food!";
                            }
                        } else {
                            $response["success"] = 0;
                            $response["message"] = "Failed to upload image!";
                        }
                    }
                }
            } else {
                $response["success"] = 0;
                $response["message"] = "Food name, quantity, or image is missing!";
            }
        } catch (PDOException $e) {
            $response["success"] = 0;
            $response["message"] = "Error while inserting: " . $e->getMessage();
        }

        echo json_encode($response);
    }
}

$insert = new InsertFood();
$insert->startInsertFood();
