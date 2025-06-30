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
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pago`
--

DROP TABLE IF EXISTS `pago`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pago` (
  `PagoID` int NOT NULL AUTO_INCREMENT,
  `TurnoID` int DEFAULT NULL,
  `Cantidad` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`PagoID`),
  KEY `TurnoID` (`TurnoID`),
  CONSTRAINT `pago_ibfk_1` FOREIGN KEY (`TurnoID`) REFERENCES `turno` (`TurnoID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-29 23:18:02
