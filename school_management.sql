-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 18, 2025 at 04:34 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `school_management`
--

-- --------------------------------------------------------

--
-- Table structure for table `assignments`
--

CREATE TABLE `assignments` (
  `id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` text NOT NULL,
  `due_date` date NOT NULL,
  `grade` varchar(50) NOT NULL,
  `subject` varchar(100) NOT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_content` longblob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `assignments`
--

INSERT INTO `assignments` (`id`, `title`, `description`, `due_date`, `grade`, `subject`, `file_name`, `file_content`) VALUES
(1, 'Test file', 'this is file', '2025-06-14', '7', 'Arabic', NULL, NULL),
(2, 'Test 2', 'hi test2', '2025-06-27', '7', 'Arabic', NULL, NULL),
(3, 'test', 'test assagnmet', '2025-06-20', '10', 'Math', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `attendance`
--

CREATE TABLE `attendance` (
  `id` int(11) NOT NULL,
  `student_id` int(11) NOT NULL,
  `subject` varchar(100) NOT NULL,
  `absence_count` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `attendance`
--

INSERT INTO `attendance` (`id`, `student_id`, `subject`, `absence_count`) VALUES
(1, 1, 'General', 1),
(2, 2, 'General', 1),
(3, 3, 'General', 1),
(4, 4, 'General', 1),
(5, 5, 'General', 1),
(6, 6, 'General', 1),
(7, 7, 'General', 1),
(8, 8, 'General', 1),
(9, 9, 'General', 1),
(10, 10, 'General', 1),
(11, 11, 'General', 1),
(12, 12, 'General', 1),
(13, 13, 'General', 1),
(14, 14, 'General', 1),
(15, 15, 'General', 1),
(16, 16, 'General', 1),
(17, 17, 'General', 1),
(18, 18, 'General', 1),
(19, 19, 'General', 1),
(20, 20, 'General', 1),
(21, 21, 'General', 1),
(22, 22, 'General', 1),
(23, 23, 'General', 1),
(24, 24, 'General', 1),
(25, 25, 'General', 1),
(26, 26, 'General', 1),
(27, 27, 'General', 1),
(28, 28, 'General', 1),
(29, 29, 'General', 1),
(30, 30, 'General', 1),
(31, 31, 'General', 1),
(32, 32, 'General', 1),
(33, 33, 'General', 1),
(34, 34, 'General', 1),
(35, 35, 'General', 1),
(36, 36, 'General', 1),
(37, 37, 'General', 1),
(38, 38, 'General', 1),
(39, 39, 'General', 1),
(110, 121, 'Arabic', 1);

-- --------------------------------------------------------

--
-- Table structure for table `chat_messages`
--

CREATE TABLE `chat_messages` (
  `id` int(11) NOT NULL,
  `sender_id` int(11) NOT NULL,
  `sender_type` enum('student','teacher') NOT NULL,
  `receiver_id` int(11) NOT NULL,
  `receiver_type` enum('student','teacher') NOT NULL,
  `message_content` text NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `chat_messages`
--

INSERT INTO `chat_messages` (`id`, `sender_id`, `sender_type`, `receiver_id`, `receiver_type`, `message_content`, `created_at`) VALUES
(1, 121, 'student', 1, 'teacher', 'hhhhhh', '2025-06-14 06:32:54'),
(2, 121, 'student', 1, 'teacher', 'gfdgfd', '2025-06-14 07:11:20'),
(3, 121, 'student', 1, 'teacher', 'dsfsd', '2025-06-14 07:15:32');

-- --------------------------------------------------------

--
-- Table structure for table `grades`
--

CREATE TABLE `grades` (
  `id` int(11) NOT NULL,
  `student_id` int(11) NOT NULL,
  `subject_name` varchar(255) NOT NULL,
  `grade` varchar(10) DEFAULT '0',
  `published` tinyint(1) DEFAULT 0,
  `teacher_id` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `grades`
--

INSERT INTO `grades` (`id`, `student_id`, `subject_name`, `grade`, `published`, `teacher_id`, `created_at`) VALUES
(1, 32, 'Math', '99', 1, 1, '2025-06-11 16:38:45'),
(2, 22, 'Math', '99', 1, 1, '2025-06-11 16:38:45'),
(3, 25, 'Math', '99', 1, 1, '2025-06-11 16:38:45'),
(4, 28, 'Math', '99', 1, 1, '2025-06-11 16:38:45'),
(5, 4, 'Math', '99', 1, 1, '2025-06-11 16:38:45'),
(6, 2, 'Math', '99', 1, 1, '2025-06-11 16:38:45'),
(7, 121, 'Arabic', '97', 1, 1, '2025-06-14 03:41:25');

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

CREATE TABLE `messages` (
  `id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `message` text NOT NULL,
  `student_id` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `messages`
--

INSERT INTO `messages` (`id`, `title`, `message`, `student_id`, `created_at`) VALUES
(1, 'test', 'hi test', 21, '2025-06-08 20:42:03'),
(3, 'test', 'hi test', 1, '2025-06-08 20:42:03'),
(5, 'test', 'hi test', 39, '2025-06-08 20:42:03'),
(6, 'test', 'hi test', 21, '2025-06-09 08:30:06'),
(7, 'hi', 'hi', 9, '2025-06-11 22:20:21'),
(8, 'hello', 'test', 9, '2025-06-11 22:23:59'),
(9, 'hello', 'test', 22, '2025-06-11 22:23:59'),
(10, 'hello', 'test', 15, '2025-06-11 22:23:59'),
(11, 'hello', 'test', 25, '2025-06-11 22:23:59'),
(12, 'hello', 'test', 28, '2025-06-11 22:23:59'),
(13, 'hi mona', 'mona , computer  since', 18, '2025-06-11 22:24:51'),
(14, 'second message', '2 messages', 18, '2025-06-11 22:25:22'),
(15, 'select all student', 'hi select all', 6, '2025-06-11 22:29:00'),
(16, 'select all student', 'hi select all', 5, '2025-06-11 22:29:00'),
(17, 'select all student', 'hi select all', 30, '2025-06-11 22:29:00'),
(18, 'select all student', 'hi select all', 24, '2025-06-11 22:29:00'),
(19, 'select all student', 'hi select all', 27, '2025-06-11 22:29:00'),
(20, 'Message from Student 1', 'abd test', 1, '2025-06-13 02:03:41'),
(21, 'Message from Student 1', 'abd test', 1, '2025-06-13 02:03:55'),
(22, 'Mr. Hassan', 'hi', 121, '2025-06-14 06:18:09'),
(23, 'Mr. Hassan', 'hello', 121, '2025-06-14 06:27:45'),
(24, 'Mr. Hassan', 'hiiiiiii', 121, '2025-06-14 06:29:19'),
(25, 'Mr. Hassan', 'hhh', 121, '2025-06-14 06:47:07'),
(26, 'Mr. Hassan', 'gfdgdfg', 121, '2025-06-14 06:47:44'),
(27, 'Mr. Hassan', 'hgfhgf', 121, '2025-06-14 07:07:15');

-- --------------------------------------------------------

--
-- Table structure for table `schedules`
--

CREATE TABLE `schedules` (
  `id` int(11) NOT NULL,
  `grade` varchar(10) NOT NULL,
  `subject` varchar(100) NOT NULL,
  `day` varchar(15) NOT NULL,
  `time` varchar(20) NOT NULL,
  `teacher_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `schedules`
--

INSERT INTO `schedules` (`id`, `grade`, `subject`, `day`, `time`, `teacher_id`) VALUES
(1, '10', 'Math', 'Monday', '09:00 AM', 1),
(4, '11', 'Science', 'Monday', '10:30 AM', 2),
(7, '12', 'Math', 'Monday', '12:00 PM', 1),
(10, '6', 'Math', 'Monday', '08:50', 1),
(11, '11', 'Arabic', 'Tuesday', '08:50', 1);

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

CREATE TABLE `students` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `grade` int(11) NOT NULL,
  `age` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `students`
--

INSERT INTO `students` (`id`, `name`, `email`, `password`, `grade`, `age`, `created_at`) VALUES
(1, 'Ali Mohammad', 'ali.mohammad@student.com', '123', 10, 16, '2025-06-08 08:05:10'),
(2, 'Sara Khaled', 'sara.khaled@student.com', '123', 11, 17, '2025-06-08 08:05:10'),
(3, 'Omar Nabil', 'omar.nabil@student.com', '123', 10, 16, '2025-06-08 08:05:10'),
(4, 'Maya Ziad', 'maya.ziad@student.com', '123', 11, 17, '2025-06-08 08:05:10'),
(5, 'Lina Hamed', 'lina.hamed@student.com', '123', 12, 18, '2025-06-08 08:05:10'),
(6, 'Jad Rami', 'jad.rami@student.com', '123', 12, 18, '2025-06-08 08:05:10'),
(7, 'Hani Salem', 'hani.salem@student.com', '123', 10, 15, '2025-06-08 08:05:10'),
(8, 'Rami Fares', 'rami.fares@student.com', '123', 10, 16, '2025-06-08 08:05:10'),
(9, 'Dina Omar', 'dina.omar@student.com', '123', 11, 17, '2025-06-08 08:05:10'),
(10, 'Nour Sami', 'nour.sami@student.com', '123', 12, 18, '2025-06-08 08:05:10'),
(11, 'Tariq Adel', 'tariq.adel@student.com', '123', 10, 16, '2025-06-08 08:05:10'),
(12, 'Sami Fadi', 'sami.fadi@student.com', '123', 11, 17, '2025-06-08 08:05:10'),
(13, 'Ruba Nader', 'ruba.nader@student.com', '123', 12, 18, '2025-06-08 08:05:10'),
(14, 'Fares Ziad', 'fares.ziad@student.com', '123', 10, 15, '2025-06-08 08:05:10'),
(15, 'Huda Sami', 'huda.sami@student.com', '123', 11, 17, '2025-06-08 08:05:10'),
(16, 'Yara Adel', 'yara.adel@student.com', '123', 12, 18, '2025-06-08 08:05:10'),
(17, 'Khaled Omar', 'khaled.omar@student.com', '123', 10, 16, '2025-06-08 08:05:10'),
(18, 'Mona Fares', 'mona.fares@student.com', '123', 11, 17, '2025-06-08 08:05:10'),
(19, 'Samer Nabil', 'samer.nabil@student.com', '123', 12, 18, '2025-06-08 08:05:10'),
(20, 'Laila Ziad', 'laila.ziad@student.com', '123', 10, 15, '2025-06-08 08:05:10'),
(21, 'Ahmed Ali', 'ahmed.ali@student.com', '123', 10, 16, '2025-06-08 08:05:10'),
(22, 'Fatima Hassan', 'fatima.hassan@student.com', '123', 11, 17, '2025-06-08 08:05:10'),
(23, 'Mohammad Omar', 'mohammad.omar@student.com', '123', 10, 16, '2025-06-08 08:05:10'),
(24, 'Sara Abdullah', 'sara.abdullah@student.com', '123', 12, 18, '2025-06-08 08:05:10'),
(25, 'Khaled Salem', 'khaled.salem@student.com', '123', 11, 17, '2025-06-08 08:05:10'),
(26, 'Nour Ali', 'nour.ali@student.com', '123', 10, 15, '2025-06-08 08:05:10'),
(27, 'Youssef Ahmed', 'youssef.ahmed@student.com', '123', 12, 17, '2025-06-08 08:05:10'),
(28, 'Mariam Said', 'mariam.said@student.com', '123', 11, 16, '2025-06-08 08:05:10'),
(29, 'Fadi Karim', 'fadi.karim@student.com', '123', 10, 15, '2025-06-08 08:05:10'),
(30, 'Reem Nader', 'reem.nader@student.com', '123', 12, 17, '2025-06-08 08:05:10'),
(31, 'Zainab Ibrahim', 'zainab.ibrahim@student.com', '123', 10, 15, '2025-06-08 08:05:10'),
(32, 'Amir Mostafa', 'amir.mostafa@student.com', '123', 11, 16, '2025-06-08 08:05:10'),
(33, 'Hana Walid', 'hana.walid@student.com', '123', 10, 15, '2025-06-08 08:05:10'),
(34, 'Tarek Fouad', 'tarek.fouad@student.com', '123', 12, 17, '2025-06-08 08:05:10'),
(35, 'Salma Adel', 'salma.adel@student.com', '123', 11, 16, '2025-06-08 08:05:10'),
(36, 'Omar Salah', 'omar.salah@student.com', '123', 10, 15, '2025-06-08 08:05:10'),
(37, 'Lana Fares', 'lana.fares@student.com', '123', 12, 17, '2025-06-08 08:05:10'),
(38, 'Bilal Majed', 'bilal.majed@student.com', '123', 11, 16, '2025-06-08 08:05:10'),
(39, 'Ayah Sami', 'ayah.sami@student.com', '123', 10, 15, '2025-06-08 08:05:10'),
(117, 'karakra', 'karakra@student.com', '123', 12, 16, '2025-06-14 03:05:17'),
(118, 'ayman', 'ayman@student.com', '123', 12, 16, '2025-06-14 03:10:42'),
(119, 'ameer', 'ameer@student.com', '123', 12, 16, '2025-06-14 03:14:49'),
(120, 'noor', 'noor@student.com', '123', 12, 16, '2025-06-14 03:36:03'),
(121, 'aya', 'aya@student.com', '123', 7, 12, '2025-06-14 03:40:32'),
(122, 'abd', 'abd@student.com', '123', 8, 14, '2025-06-14 05:15:35'),
(123, 'HASSAN', 'has@student.com', '123', 11, 15, '2025-06-14 05:29:24'),
(124, 'omar', 'omar@student.com', '123', 12, 16, '2025-06-14 07:46:01');

-- --------------------------------------------------------

--
-- Table structure for table `student_subjects`
--

CREATE TABLE `student_subjects` (
  `id` int(11) NOT NULL,
  `student_id` int(11) DEFAULT NULL,
  `subject` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `student_subjects`
--

INSERT INTO `student_subjects` (`id`, `student_id`, `subject`) VALUES
(1, 1, 'Math'),
(2, 1, 'Science'),
(3, 1, 'English'),
(4, 2, 'Math'),
(5, 2, 'English'),
(6, 3, 'Science'),
(7, 3, 'English'),
(8, 4, 'Math'),
(9, 4, 'Science'),
(10, 5, 'Math'),
(11, 5, 'English'),
(12, 6, 'Science'),
(13, 6, 'English'),
(14, 7, 'Math'),
(15, 7, 'Science'),
(16, 8, 'Math'),
(17, 8, 'English'),
(18, 9, 'Science'),
(19, 9, 'English'),
(20, 10, 'Math'),
(21, 10, 'Science'),
(22, 11, 'Math'),
(23, 11, 'English'),
(24, 12, 'Science'),
(25, 12, 'English'),
(26, 13, 'Math'),
(27, 13, 'Science'),
(28, 14, 'Math'),
(29, 14, 'English'),
(30, 15, 'Science'),
(31, 15, 'English'),
(32, 16, 'Math'),
(33, 16, 'Science'),
(34, 17, 'Math'),
(35, 17, 'English'),
(36, 18, 'Science'),
(37, 18, 'English'),
(38, 19, 'Math'),
(39, 19, 'Science'),
(40, 20, 'Math'),
(41, 20, 'English'),
(42, 21, 'Science'),
(43, 21, 'English'),
(44, 22, 'Math'),
(45, 22, 'Science'),
(46, 23, 'Math'),
(47, 23, 'English'),
(48, 24, 'Science'),
(49, 24, 'English'),
(50, 25, 'Math'),
(51, 25, 'Science'),
(52, 26, 'Math'),
(53, 26, 'English'),
(54, 27, 'Science'),
(55, 27, 'English'),
(56, 28, 'Math'),
(57, 28, 'Science'),
(58, 29, 'Math'),
(59, 29, 'English'),
(60, 30, 'Science'),
(61, 30, 'English'),
(62, 31, 'Math'),
(63, 31, 'Science'),
(64, 32, 'Math'),
(65, 32, 'English'),
(66, 33, 'Science'),
(67, 33, 'English'),
(68, 34, 'Math'),
(69, 34, 'Science'),
(70, 120, 'Arabic'),
(71, 121, 'Arabic'),
(72, 122, 'English'),
(73, 123, 'Arabic'),
(74, 124, 'Math');

-- --------------------------------------------------------

--
-- Table structure for table `submissions`
--

CREATE TABLE `submissions` (
  `id` int(11) NOT NULL,
  `student_id` int(11) DEFAULT NULL,
  `assignment_id` int(11) DEFAULT NULL,
  `content` text DEFAULT NULL,
  `submitted_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `submissions`
--

INSERT INTO `submissions` (`id`, `student_id`, `assignment_id`, `content`, `submitted_at`) VALUES
(1, 1, 1, 'All algebra questions solved', '2025-06-08 07:40:19'),
(2, 2, 4, 'Completed renewable energy report', '2025-06-08 07:40:19'),
(3, 5, 7, 'Calculus questions done', '2025-06-08 07:40:19'),
(4, 6, 9, 'Business plan document uploaded', '2025-06-08 07:40:19'),
(5, 1, 10, 'abd test', '2025-06-13 01:14:36'),
(6, 121, 1, 'done', '2025-06-14 05:58:57'),
(7, 1, 3, 'solve', '2025-06-14 08:59:04');

-- --------------------------------------------------------

--
-- Table structure for table `teachers`
--

CREATE TABLE `teachers` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `subject` varchar(255) NOT NULL,
  `day` varchar(50) DEFAULT NULL,
  `time` varchar(50) DEFAULT NULL,
  `grade` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `teachers`
--

INSERT INTO `teachers` (`id`, `name`, `email`, `password`, `subject`, `day`, `time`, `grade`) VALUES
(1, 'Mr. Hassan', 'hassan@school.com', '123', 'Arabic', 'Monday', '08:00 AM', 'Grade 9'),
(2, 'Ms. Lina', 'omar@school.com', '123', 'Math', 'Tuesday', '09:00 AM', 'Grade 10'),
(3, 'Mr. Sami Taha', 'abd@school.com', '123', 'Science', 'Wednesday', '10:00 AM', 'Grade 11'),
(4, 'Ms. Huda Nasr', 'faisal@school.com', '123', 'English', 'Thursday', '11:00 AM', 'Grade 12');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `assignments`
--
ALTER TABLE `assignments`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `attendance`
--
ALTER TABLE `attendance`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `student_subject_unique` (`student_id`,`subject`);

--
-- Indexes for table `chat_messages`
--
ALTER TABLE `chat_messages`
  ADD PRIMARY KEY (`id`),
  ADD KEY `sender_id` (`sender_id`,`sender_type`),
  ADD KEY `receiver_id` (`receiver_id`,`receiver_type`);

--
-- Indexes for table `grades`
--
ALTER TABLE `grades`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_student_subject_grade` (`student_id`,`subject_name`),
  ADD KEY `teacher_id` (`teacher_id`);

--
-- Indexes for table `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`id`),
  ADD KEY `student_id` (`student_id`);

--
-- Indexes for table `schedules`
--
ALTER TABLE `schedules`
  ADD PRIMARY KEY (`id`),
  ADD KEY `teacher_id` (`teacher_id`);

--
-- Indexes for table `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `student_subjects`
--
ALTER TABLE `student_subjects`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `submissions`
--
ALTER TABLE `submissions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `student_id` (`student_id`),
  ADD KEY `assignment_id` (`assignment_id`);

--
-- Indexes for table `teachers`
--
ALTER TABLE `teachers`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `assignments`
--
ALTER TABLE `assignments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `attendance`
--
ALTER TABLE `attendance`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=111;

--
-- AUTO_INCREMENT for table `chat_messages`
--
ALTER TABLE `chat_messages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `grades`
--
ALTER TABLE `grades`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `messages`
--
ALTER TABLE `messages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT for table `schedules`
--
ALTER TABLE `schedules`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `students`
--
ALTER TABLE `students`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=125;

--
-- AUTO_INCREMENT for table `student_subjects`
--
ALTER TABLE `student_subjects`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=75;

--
-- AUTO_INCREMENT for table `submissions`
--
ALTER TABLE `submissions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `teachers`
--
ALTER TABLE `teachers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `attendance`
--
ALTER TABLE `attendance`
  ADD CONSTRAINT `attendance_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `grades`
--
ALTER TABLE `grades`
  ADD CONSTRAINT `grades_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `grades_ibfk_2` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`id`) ON DELETE SET NULL;

--
-- Constraints for table `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `schedules`
--
ALTER TABLE `schedules`
  ADD CONSTRAINT `schedules_ibfk_1` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`id`) ON DELETE SET NULL;

--
-- Constraints for table `submissions`
--
ALTER TABLE `submissions`
  ADD CONSTRAINT `submissions_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `submissions_ibfk_2` FOREIGN KEY (`assignment_id`) REFERENCES `assignments` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
