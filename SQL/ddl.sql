CREATE TABLE Member (
	memberID	SERIAL		PRIMARY KEY,
	fName		VARCHAR(50)	NOT NULL,
	lName		VARCHAR(50) 	NOT NULL,
	email		VARCHAR(50)	NOT NULL  	UNIQUE,
	username	VARCHAR(50) 	NOT NULL	UNIQUE,
	password	VARCHAR(255) 	NOT NULL,
	sex		VARCHAR(25),
	birthday	DATE		NOT NULL,
	weight		INT		NOT NULL,
	height		INT		NOT NULL,
	fitnessGoal	VARCHAR(500)
);


CREATE TABLE fitnessAchievement (
	memberID	INT NOT NULL,		
	achievement VARCHAR(500), 
	FOREIGN KEY (memberID)
		REFERENCES Member (memberID)
);

CREATE TABLE Routine (
	routineID	SERIAL		PRIMARY KEY,
	routineName	VARCHAR(255)	NOT NULL,
	description 	VARCHAR(500)
);


CREATE TABLE HasRoutine (
	memberID	INT 	NOT NULL,		
	routineID	INT		NOT NULL, 
	FOREIGN KEY (memberID)
		REFERENCES Member (memberID),
	FOREIGN KEY (routineID)
		REFERENCES Routine (routineID)
);


CREATE TABLE Trainer (
	trainerID	SERIAL		PRIMARY KEY,
	fName		VARCHAR(50)	NOT NULL,
	lName		VARCHAR(50) 	NOT NULL,
	email		VARCHAR(50)	NOT NULL  	UNIQUE,
	username	VARCHAR(50) 	NOT NULL	UNIQUE,
	password	VARCHAR(255) 	NOT NULL,
	sex		VARCHAR(25)
);


CREATE TABLE TrainingSessions (
	sessionID	SERIAL		PRIMARY KEY,
	trainerID	INT		NOT NULL,
	sessionName	VARCHAR(255)	NOT NULL,
	type		VARCHAR(25)	NOT NULL,
	date		DATE		NOT NULL,
	startTime	TIME		NOT NULL,
	endTime		TIME		NOT NULL,
	FOREIGN KEY (trainerID)
		REFERENCES Trainer (trainerID)
);


CREATE TABLE SessionParticipants (
	sessionID	INT	NOT NULL,
	memberID	INT NOT NULL,
	FOREIGN KEY (sessionID)
		REFERENCES TrainingSessions (sessionID),
	FOREIGN KEY (memberID)
		REFERENCES Member (memberID)
);


CREATE TABLE Availability (
	availabilityID	SERIAL	PRIMARY KEY,
	trainerID	INT	NOT NULL,
	date		DATE,
	startTime	TIME,
	endTime		TIME,
	FOREIGN KEY (trainerID)
		REFERENCES Trainer (trainerID)
);


CREATE TABLE Admin (
	adminID		SERIAL		PRIMARY KEY,
	fName		VARCHAR(50)	NOT NULL,
	lName		VARCHAR(50) 	NOT NULL,
	email		VARCHAR(50)	NOT NULL  	UNIQUE,
	username	VARCHAR(50) 	NOT NULL	UNIQUE,
	password	VARCHAR(255) 	NOT NULL
);


CREATE TABLE Equipments (
	equipmentID	SERIAL		PRIMARY KEY,
	equipmentName	VARCHAR(50)	NOT NULL,
	lastMaintenence	DATE
);


CREATE TABLE Rooms (
	roomID		SERIAL		PRIMARY KEY,
	roomName	VARCHAR(50)	NOT NULL
);


CREATE TABLE RoomBooking (
	bookingID	SERIAL	PRIMARY KEY,
	roomID		INT	NOT NULL,
	adminID		INT 	NOT NULL,
	date		DATE	NOT NULL,
	startTime	TIME	NOT NULL,
	endTime		TIME	NOT NULL,
	FOREIGN KEY (roomID)
		REFERENCES Rooms (roomID),
	FOREIGN KEY (adminID)
		REFERENCES Admin (adminID)
);


CREATE TABLE Billing (
	billingID	SERIAL	PRIMARY KEY,
	memberID	INT	NOT NULL,
	adminID  	INT 	NOT NULL,
	amount		FLOAT 	NOT NULL,
	date		DATE	NOT NULL,
	FOREIGN KEY (memberID)
		REFERENCES Member (memberID),
	FOREIGN KEY (adminID)
		REFERENCES Admin (adminID)
		
);