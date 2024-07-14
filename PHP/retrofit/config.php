<?php
class Connection{
    function getConnection(){
        $host       = "localhost";
        $username   = "u726159739_test_app";
        $password   = "b&P2xbd9SQ08";
        $dbname     = "u726159739_test_app";

        try{
            $conn    = new PDO("mysql:host=$host;dbname=$dbname", $username, $password);
            $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            return $conn;
        }catch (PDOException $e){
            echo "ERROR CONNECTIONF : " . $e->getMessage();
        }
    }
}