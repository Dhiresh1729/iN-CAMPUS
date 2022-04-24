<?php

    require_once'../includes/DbOperations.php';
    $response = array();
    if($_SERVER['REQUEST_METHOD'] == 'POST'){

        if(
             isset($_POST['username']) and
             isset($_POST['time']) and
             isset($_POST['exit_entry'])
        ){

            $db = new DbOperations();
            if($db->insert_log(
                $_POST['username'],
                $_POST['time'],
                $_POST['exit_entry']
            )){
                $response['error'] = false;
                $response['message'] = "Log inserted successfully";
            }else{
                $response['error'] = true;
                $response['message'] = "Some error occured please try again";
            }

        }else{

            $response['error'] = true;
            $response['message'] = "Requierd fields are missing";

        }

    }else{

        $response['error'] = true;
        $response['message'] = "Invalid Request";

    }

    echo json_encode($response);