INSERT INTO Member (fName, lName, email, username, password, sex, birthday, weight, height, fitnessGoal, fitnessAchievement) 
VALUES 
('User1 FName', 'User1 LName', 'user1@example.com', 'user1', 'password', 'Male', '1990-01-01', 70, 175, 'Increase stamina', 'Ran 5k'),
('User2 FName', 'User2 LName', 'user2@example.com', 'user2', 'password', 'Female', '1992-02-02', 65, 165, 'Build muscle', 'Gained 2kg muscle');

INSERT INTO Routine (routineName, memberID, description) 
VALUES 
('Routine1', 1, 'Routine 1 description'),
('Routine2', 2, 'Routine 2 description');

INSERT INTO Trainer (fName, lName, email, username, password, sex) 
VALUES 
('Trainer1 FName', 'Trainer1 LName', 'trainer1@example.com', 'trainer1', 'password', 'Male'),
('Trainer2 FName', 'Trainer2 LName', 'trainer2@example.com', 'trainer2', 'password', 'Female');

INSERT INTO TrainingSessions (trainerID, sessionName, type, date, startTime, endTime) 
VALUES 
(1, 'Session1', 'Type1', '2024-04-10', '08:00', '09:00'),
(2, 'Session2', 'Type2', '2024-04-11', '10:00', '11:00');

INSERT INTO SessionParticipants (sessionID, memberID) 
VALUES 
(1, 1),
(2, 2);

INSERT INTO Availability (trainerID, date, startTime, endTime) 
VALUES 
(1, '2024-04-12', '09:00', '11:00'),
(2, '2024-04-13', '12:00', '14:00');

INSERT INTO Admin (fName, lName, email, username, password) 
VALUES 
('Admin1 FName', 'Admin1 LName', 'admin1@example.com', 'admin1', 'password');

INSERT INTO Equipments (equipmentName, lastMaintenence) 
VALUES 
('Equipment1', '2024-03-01'),
('Equipment2', '2024-03-15');

INSERT INTO Rooms (roomName) 
VALUES 
('Room1'),
('Room2');

INSERT INTO RoomBooking (roomID, date, startTime, endTime) 
VALUES 
(1, '2024-04-14', '09:00', '10:00'),
(2, '2024-04-15', '11:00', '12:00');

INSERT INTO Billing (memberID, amount, date) 
VALUES 
(1, 100, '2024-04-20'),
(2, 200, '2024-04-21');