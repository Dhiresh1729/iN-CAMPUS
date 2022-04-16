<?php

    class DbOperations{

        private $con;

        function __construct(){

            require_once dirname(__FILE__).'/DbConnect.php';

            $db = new DbConnect();

            $this->con = $db->connect();

        }

        /*CRUD -> c -> CREATE*/

        public function insert_log($username, $time, $exit_entry){

            $stmt = $this->con->prepare("INSERT INTO `all_user_logs` (`user_name`, `time`, `exit_entry`) VALUES(?, ?, ?);");
            $stmt->bind_param("sss", $username, $time, $exit_entry);

            if($stmt->execute()){
                return true;
            }else{
                return false;
            }
        }

        public function getUserLogs($username){
            $sql = "SELECT DATE_FORMAT(time, '%d/%m/%y %r'), exit_entry FROM all_user_logs WHERE user_name = '$username' ORDER BY time ASC";
            $result = mysqli_query($this->con, $sql);
            return $result;
        }

        public function getAllUserLogs(){
            $sql = "SELECT user_name, DATE_FORMAT(time, '%d/%m/%y %r'), exit_entry FROM all_user_logs";
            $result = mysqli_query($this->con, $sql);
            return $result;
        }
    }