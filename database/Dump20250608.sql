-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: odontologia
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `especialidad`
--

DROP TABLE IF EXISTS `especialidad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `especialidad` (
  `EspecialidadID` int NOT NULL AUTO_INCREMENT,
  `OdontologoDNI` int DEFAULT NULL,
  `Nombre` varchar(100) NOT NULL,
  `Descripcion` text,
  PRIMARY KEY (`EspecialidadID`),
  KEY `OdontologoDNI` (`OdontologoDNI`),
  CONSTRAINT `especialidad_ibfk_1` FOREIGN KEY (`OdontologoDNI`) REFERENCES `odontologo` (`DNI`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `especialidad`
--

LOCK TABLES `especialidad` WRITE;
/*!40000 ALTER TABLE `especialidad` DISABLE KEYS */;
INSERT INTO `especialidad` VALUES (3,10000007,'Periodoncia','Tratamiento de encías y estructuras de soporte'),(6,NULL,'Ortodoncia Infantil','Para menores, aparatos de los que se pueden quitar'),(16,10000005,'Ortodoncia',NULL),(17,10000005,'Periodoncia',NULL),(18,39698949,'Ortodoncia',NULL),(19,39698949,'Ortodoncia Infantil',NULL),(20,39698949,'Periodoncia',NULL),(21,39698948,'Ortodoncia',NULL),(22,38996655,'Ortodoncia Infantil',NULL),(23,38598788,'Ortodoncia',NULL),(26,39698945,'Ortodoncia',NULL),(27,39698945,'Ortodoncia Infantil',NULL);
/*!40000 ALTER TABLE `especialidad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `historialclinica`
--

DROP TABLE IF EXISTS `historialclinica`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `historialclinica` (
  `HistorialClinicaID` int NOT NULL AUTO_INCREMENT,
  `PacienteID` int DEFAULT NULL,
  `OdontologoDNI` int DEFAULT NULL,
  `Fecha` date DEFAULT NULL,
  `Descripcion` text,
  PRIMARY KEY (`HistorialClinicaID`),
  KEY `PacienteID` (`PacienteID`),
  KEY `OdontologoDNI` (`OdontologoDNI`),
  CONSTRAINT `historialclinica_ibfk_1` FOREIGN KEY (`PacienteID`) REFERENCES `paciente` (`PacienteID`),
  CONSTRAINT `historialclinica_ibfk_2` FOREIGN KEY (`OdontologoDNI`) REFERENCES `odontologo` (`DNI`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historialclinica`
--

LOCK TABLES `historialclinica` WRITE;
/*!40000 ALTER TABLE `historialclinica` DISABLE KEYS */;
INSERT INTO `historialclinica` VALUES (1,1,10000005,'2025-05-20','Revisión general y limpieza dental.'),(2,2,10000006,'2025-05-21','Tratamiento de caries en molar inferior derecho.');
/*!40000 ALTER TABLE `historialclinica` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `horarioodontologo`
--

DROP TABLE IF EXISTS `horarioodontologo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `horarioodontologo` (
  `OdontologoDNI` int NOT NULL,
  `Dia` enum('Lunes','Martes','Miércoles','Jueves','Viernes','Sábado','Domingo') NOT NULL,
  `HoraInicio` time NOT NULL,
  `HoraFin` time NOT NULL,
  PRIMARY KEY (`OdontologoDNI`,`Dia`),
  CONSTRAINT `horarioodontologo_ibfk_1` FOREIGN KEY (`OdontologoDNI`) REFERENCES `odontologo` (`DNI`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `horarioodontologo`
--

LOCK TABLES `horarioodontologo` WRITE;
/*!40000 ALTER TABLE `horarioodontologo` DISABLE KEYS */;
INSERT INTO `horarioodontologo` VALUES (10000005,'Lunes','08:00:00','12:00:00'),(10000005,'Miércoles','08:00:00','12:00:00'),(10000005,'Viernes','08:00:00','12:00:00'),(10000007,'Lunes','10:00:00','14:00:00'),(10000007,'Miércoles','10:00:00','14:00:00'),(10000007,'Viernes','10:00:00','14:00:00'),(39698945,'Lunes','08:00:00','12:00:00');
/*!40000 ALTER TABLE `horarioodontologo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `odontologo`
--

DROP TABLE IF EXISTS `odontologo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `odontologo` (
  `DNI` int NOT NULL,
  `Matricula` varchar(50) NOT NULL,
  `Usuario` varchar(50) NOT NULL,
  `Contrasenia` varchar(50) NOT NULL,
  PRIMARY KEY (`DNI`),
  UNIQUE KEY `Usuario` (`Usuario`),
  CONSTRAINT `odontologo_ibfk_1` FOREIGN KEY (`DNI`) REFERENCES `persona` (`DNI`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `odontologo`
--

LOCK TABLES `odontologo` WRITE;
/*!40000 ALTER TABLE `odontologo` DISABLE KEYS */;
INSERT INTO `odontologo` VALUES (10000005,'M001','mdiaz','clave001'),(10000006,'M002','pfernandez','clave002'),(10000007,'M003','lramirez','clave003'),(38598788,'RRR222','flor','123'),(38996655,'RRR555','rgomez','123'),(39698945,'RRR555','flor123','123'),(39698948,'Val123','valu','123'),(39698949,'VAL123','val','123');
/*!40000 ALTER TABLE `odontologo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paciente`
--

DROP TABLE IF EXISTS `paciente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `paciente` (
  `PacienteID` int NOT NULL AUTO_INCREMENT,
  `DNI` int DEFAULT NULL,
  `ObraSocial` varchar(100) DEFAULT NULL,
  `NumeroAfiliado` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`PacienteID`),
  KEY `DNI` (`DNI`),
  CONSTRAINT `paciente_ibfk_1` FOREIGN KEY (`DNI`) REFERENCES `persona` (`DNI`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paciente`
--

LOCK TABLES `paciente` WRITE;
/*!40000 ALTER TABLE `paciente` DISABLE KEYS */;
INSERT INTO `paciente` VALUES (1,10000001,'OSDE','OSDE1234'),(2,10000002,'Sancor','SANC457');
/*!40000 ALTER TABLE `paciente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pago`
--

DROP TABLE IF EXISTS `pago`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pago` (
  `PagoID` int NOT NULL,
  `TurnoID` int DEFAULT NULL,
  `Cantidad` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`PagoID`),
  KEY `TurnoID` (`TurnoID`),
  CONSTRAINT `pago_ibfk_1` FOREIGN KEY (`TurnoID`) REFERENCES `turno` (`TurnoID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pago`
--

LOCK TABLES `pago` WRITE;
/*!40000 ALTER TABLE `pago` DISABLE KEYS */;
INSERT INTO `pago` VALUES (1,1,3500.00);
/*!40000 ALTER TABLE `pago` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `persona`
--

DROP TABLE IF EXISTS `persona`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `persona` (
  `DNI` int NOT NULL,
  `Nombre` varchar(100) DEFAULT NULL,
  `Apellido` varchar(100) DEFAULT NULL,
  `FechaNacimiento` date DEFAULT NULL,
  `Telefono` varchar(20) DEFAULT NULL,
  `Genero` varchar(10) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`DNI`),
  CONSTRAINT `persona_chk_1` CHECK ((`DNI` between 1000000 and 99999999))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `persona`
--

LOCK TABLES `persona` WRITE;
/*!40000 ALTER TABLE `persona` DISABLE KEYS */;
INSERT INTO `persona` VALUES (10000001,'Juan','Perez','1985-01-01','123456789','Masculino','juan.perez@mail.com'),(10000002,'Ana','Lopez','1990-05-12','987654321','Femenino','ana.lopez1@mail.com'),(10000003,'Lucia','Martinez','1992-07-20','111222333','Femenino','lucia.martinez@mail.com'),(10000004,'Carlos','Gomez','1988-03-15','444555666','Masculino','carlos.gomez@mail.com'),(10000005,'Mariana','Diaz','1980-10-10','555666777','Femenino','mariana.diaz11@mail.com'),(10000006,'Pedro','Fernandez','1979-12-01','666777888','Masculino','pedro.fernandez@mail.com'),(10000007,'Laura','Ramirez','1983-06-18','777888999','Femenino','laura.ramirez@mail.com'),(12345678,'Juan','Perez','1995-01-01','123456789','M','juan@example.com'),(38598788,'Florencia','Roca','1996-06-11','3815978654','Femenino','flor@gmail.com'),(38996655,'Roberta','Gomez','1995-01-11','3815440523','Femenino','roberta@gmail.com'),(39698940,'valentina','cesca','1996-06-11','3815440523','Femenino','valentinacesca.utn@gmail.com'),(39698945,'flor','roca','1996-06-01','3815440545','Femenino','flor1@gmail.com'),(39698948,'Valen','Cesca','2000-06-11','3815440523','Femenino','valencesca1@gmail.com'),(39698949,'valentina','cesca','1996-06-11','3815440523','Femenino','valencesca@gmail.com');
/*!40000 ALTER TABLE `persona` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `secretaria`
--

DROP TABLE IF EXISTS `secretaria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `secretaria` (
  `SecretariaID` int NOT NULL,
  `DNI` int DEFAULT NULL,
  `Usuario` varchar(50) DEFAULT NULL,
  `Contrasenia` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`SecretariaID`),
  KEY `DNI` (`DNI`),
  CONSTRAINT `secretaria_ibfk_1` FOREIGN KEY (`DNI`) REFERENCES `persona` (`DNI`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `secretaria`
--

LOCK TABLES `secretaria` WRITE;
/*!40000 ALTER TABLE `secretaria` DISABLE KEYS */;
INSERT INTO `secretaria` VALUES (1,10000003,'lmartinez','clave123'),(2,10000004,'cgomez','clave456');
/*!40000 ALTER TABLE `secretaria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `turno`
--

DROP TABLE IF EXISTS `turno`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `turno` (
  `TurnoID` int NOT NULL AUTO_INCREMENT,
  `PacienteID` int DEFAULT NULL,
  `OdontologoDNI` int DEFAULT NULL,
  `SecretariaID` int DEFAULT NULL,
  `PagoID` int DEFAULT NULL,
  `FechaProgramada` date DEFAULT NULL,
  `HoraProgramada` time DEFAULT NULL,
  PRIMARY KEY (`TurnoID`),
  KEY `PacienteID` (`PacienteID`),
  KEY `OdontologoDNI` (`OdontologoDNI`),
  KEY `SecretariaID` (`SecretariaID`),
  KEY `FK_Turno_Pago` (`PagoID`),
  CONSTRAINT `FK_Turno_Pago` FOREIGN KEY (`PagoID`) REFERENCES `pago` (`PagoID`),
  CONSTRAINT `turno_ibfk_1` FOREIGN KEY (`PacienteID`) REFERENCES `paciente` (`PacienteID`),
  CONSTRAINT `turno_ibfk_2` FOREIGN KEY (`OdontologoDNI`) REFERENCES `odontologo` (`DNI`),
  CONSTRAINT `turno_ibfk_3` FOREIGN KEY (`SecretariaID`) REFERENCES `secretaria` (`SecretariaID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `turno`
--

LOCK TABLES `turno` WRITE;
/*!40000 ALTER TABLE `turno` DISABLE KEYS */;
INSERT INTO `turno` VALUES (1,1,10000005,1,NULL,'2025-05-20','09:00:00'),(2,2,10000006,2,NULL,'2025-05-21','10:00:00');
/*!40000 ALTER TABLE `turno` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'odontologia'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-08 11:14:24
