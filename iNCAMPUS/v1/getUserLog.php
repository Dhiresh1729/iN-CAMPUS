<?php

    require_once'../includes/DbOperations.php';
    $response = array();

    if($_SERVER['REQUEST_METHOD'] =='POST'){
        if(isset($_POST['user_name'])){
            $db = new DbOperations();
            $result = $db->getUserLogs($_POST['user_name']);

            $res = [];
            while($row = mysqli_fetch_array($result, MYSQLI_NUM)){
                array_push($res, $row[0]." ".$row[1]);
            }

            $response["error"] = false;
            $response["logs"] = $res;

        }else{
            $response["erro"] = true;
            $response["message"] = "Required fileds are missing";
        }
    }

echo json_encode($response);