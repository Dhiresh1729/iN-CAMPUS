<?php

    require_once'../includes/DbOperations.php';
    $response = array();

    if($_SERVER['REQUEST_METHOD'] =='POST'){
            $db = new DbOperations();
            $result = $db->getAllUserLogs();

            $res = [];
            while($row = mysqli_fetch_array($result, MYSQLI_NUM)){
                array_push($res, $row[0]." ".$row[1]." ".$row[2]);
            }

            $response["error"] = false;
            $response["logs"] = $res;

        
    }

echo json_encode($response);