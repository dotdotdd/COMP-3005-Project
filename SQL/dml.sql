INSERT INTO Member (fName, lName, email, username, password, sex, birthday, weight, height, fitnessGoal) VALUES
('Alice', 'Smith', 'alice@example.com', 'user1', 'password', 'Female', '1995-01-01', 60, 165, 'Increase endurance'),
('Bob', 'Jones', 'bob@example.com', 'user2', 'password', 'Male', '1980-02-12', 90, 175, 'Lose weight'),
('Carol', 'Taylor', 'carol@example.com', 'user3', 'password', 'Female', '1988-03-23', 55, 162, 'Build muscle'),
('Dave', 'Brown', 'dave@example.com', 'user4', 'password', 'Male', '1992-04-04', 85, 182, 'Improve flexibility'),
('Eve', 'Davis', 'eve@example.com', 'user5', 'password', 'Female', '1990-05-15', 65, 170, 'General fitness');

INSERT INTO fitnessAchievement (memberID, achievement) VALUES
(1, 'Completed first 5k run'),
(1, 'Lost 10kg in 3 months'),
(3, 'Joined a local fitness club'),
(4, 'Participated in a marathon'),
(5, 'Started a yoga course');

INSERT INTO Routine (routineName, description) VALUES
('Beginner Fitness', 'A routine for fitness beginners.'),
('Intermediate Fitness', 'A step up for those with some fitness experience.'),
('Advanced Fitness', 'High-intensity workouts for advanced individuals.'),
('Yoga Basics', 'Introduction to basic yoga practices.'),
('Marathon Prep', 'A comprehensive plan for preparing for a marathon.');

INSERT INTO HasRoutine (memberID, routineID) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5);

INSERT INTO Trainer (fName, lName, email, username, password, sex) VALUES
('Frank', 'Howard', 'frank@example.com', 'trainer1', 'password', 'Male'),
('Gina', 'Carlyle', 'gina@example.com', 'trainer2', 'password', 'Female'),
('Harry', 'Ives', 'harry@example.com', 'trainer3', 'password', 'Male'),
('Irene', 'Joyce', 'irene@example.com', 'trainer4', 'password', 'Female'),
('Jack', 'Kline', 'jack@example.com', 'trainer5', 'password', 'Male');

INSERT INTO TrainingSessions (trainerID, sessionName, type, date, startTime, endTime) VALUES
(1, 'Morning Yoga', 'Yoga', '2024-04-20', '07:00', '08:00'),
(2, 'Lunchtime HIIT', 'HIIT', '2024-04-21', '12:00', '12:45'),
(3, 'Evening Run', 'Cardio', '2024-04-22', '18:00', '19:00'),
(4, 'Strength Training', 'Strength', '2024-04-23', '17:00', '18:30'),
(5, 'Boxing Basics', 'Boxing', '2024-04-24', '16:00', '17:00');

INSERT INTO SessionParticipants (sessionID, memberID) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5);

INSERT INTO Availability (trainerID, date, startTime, endTime) VALUES
(1, '2024-04-25', '09:00', '11:00'),
(2, '2024-04-20', '07:00', '08:00'),
(2, '2024-04-26', '10:00', '12:00'),
(3, '2024-04-27', '13:00', '15:00'),
(4, '2024-04-28', '14:00', '16:00'),
(5, '2024-04-29', '15:00', '17:00');

INSERT INTO Admin (fName, lName, email, username, password) VALUES
('Karen', 'Lowell', 'karen@example.com', 'admin1', 'password'),
('Leon', 'Miles', 'leon@example.com', 'admin2', 'password'),
('Mona', 'Nash', 'mona@example.com', 'admin3', 'password'),
('Nigel', 'Orr', 'nigel@example.com', 'admin4', 'password'),
('Opal', 'Pitts', 'opal@example.com', 'admin5', 'password');

INSERT INTO Equipments (equipmentName, lastMaintenence) VALUES
('Treadmill', '2024-03-01'),
('Stationary Bicycle', '2024-03-05'),
('Rowing Machine', '2024-03-10'),
('Elliptical Machine', '2024-03-15'),
('Free Weights', '2024-03-20');

INSERT INTO Rooms (roomName) VALUES
('Cardio Room'),
('Strength Training Room'),
('Yoga Studio'),
('Spin Class Room'),
('General Purpose Room');

INSERT INTO RoomBooking (roomID, adminID, date, startTime, endTime) VALUES
(1, 1, '2024-05-01', '09:00', '10:00'),
(2, 2, '2024-05-02', '10:00', '11:00'),
(3, 3, '2024-05-03', '11:00', '12:00'),
(4, 4, '2024-05-04', '12:00', '13:00'),
(5, 5, '2024-05-05', '13:00', '14:00');

INSERT INTO Billing (memberID, adminID, amount, date) VALUES
(1, 1, 50.00, '2024-04-30'),
(2, 2, 75.00, '2024-04-30'),
(3, 3, 20.00, '2024-04-30'),
(4, 4, 100.00, '2024-04-30'),
(5, 5, 150.00, '2024-04-30');