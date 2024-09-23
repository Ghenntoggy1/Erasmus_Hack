-- public.category definition

-- Drop table

-- DROP TABLE IF EXISTS public.category;

CREATE TABLE public.category (
	category_id serial4 NOT NULL,
	category_name varchar(255) NULL,
	CONSTRAINT category_pkey PRIMARY KEY (category_id)
);


-- public.country definition

-- Drop table

-- DROP TABLE public.country;

CREATE TABLE public.country (
	country_id varchar(255) NOT NULL,
	country_name varchar(255) NULL,
	CONSTRAINT country_pkey PRIMARY KEY (country_id)
);


-- public.course definition

-- Drop table

-- DROP TABLE public.course;

CREATE TABLE public.course (
	course_id serial4 NOT NULL,
	course_credits int4 NULL,
	course_name varchar(255) NULL,
	course_semester int4 NULL,
	CONSTRAINT course_pkey PRIMARY KEY (course_id)
);


-- public.city definition

-- Drop table

-- DROP TABLE public.city;

CREATE TABLE public.city (
	city_id serial4 NOT NULL,
	city_name varchar(255) NULL,
	country_id varchar(255) NULL,
	CONSTRAINT city_pkey PRIMARY KEY (city_id),
	CONSTRAINT fklrebnlrl8vmsv1ptjnkl3qm59 FOREIGN KEY (country_id) REFERENCES public.country(country_id)
);


-- public.university definition

-- Drop table

-- DROP TABLE public.university;

CREATE TABLE public.university (
	university_id serial4 NOT NULL,
	university_name varchar(255) NULL,
	city_id int4 NULL,
	CONSTRAINT university_pkey PRIMARY KEY (university_id),
	CONSTRAINT fk7sydasl711rx7sm5u9i808wmu FOREIGN KEY (city_id) REFERENCES public.city(city_id)
);


-- public.offer definition

-- Drop table

-- DROP TABLE public.offer;

CREATE TABLE public.offer (
	offer_id serial4 NOT NULL,
	description varchar(255) NULL,
	"language" varchar(255) NULL,
	offer_name varchar(255) NULL,
	end_date date NULL,
	start_date date NULL,
	program_end date NULL,
	program_start date NULL,
	scholarship int4 NULL,
	receiver_id int4 NULL,
	sender_id int4 NULL,
	CONSTRAINT offer_pkey PRIMARY KEY (offer_id),
	CONSTRAINT uk_cbp49ogprwb2ledjij0g3wi9i UNIQUE (sender_id),
	CONSTRAINT fk2qg8k399ml494427fwxtjpi2p FOREIGN KEY (receiver_id) REFERENCES public.university(university_id),
	CONSTRAINT fks7kgd8j66darglwvmqdd5bp6f FOREIGN KEY (sender_id) REFERENCES public.university(university_id)
);


-- public.specialization definition

-- Drop table

-- DROP TABLE public.specialization;

CREATE TABLE public.specialization (
	specialization_id serial4 NOT NULL,
	specialization_name varchar(255) NULL,
	category_id int4 NULL,
	university_id int4 NULL,
	CONSTRAINT specialization_pkey PRIMARY KEY (specialization_id),
	CONSTRAINT fkleikiej592xyn1947aar26rgq FOREIGN KEY (category_id) REFERENCES public.category(category_id),
	CONSTRAINT fkse5iik2hp4imp9g853uhqqfdd FOREIGN KEY (university_id) REFERENCES public.university(university_id)
);


-- public.specialization_course definition

-- Drop table

-- DROP TABLE public.specialization_course;

CREATE TABLE public.specialization_course (
	specialization_id int4 NOT NULL,
	course_id int4 NOT NULL,
	CONSTRAINT fkekjkwlxk28vd5i28qhrpikjxp FOREIGN KEY (course_id) REFERENCES public.course(course_id),
	CONSTRAINT fkjn3gqbtgglffpu389ietl9ivy FOREIGN KEY (specialization_id) REFERENCES public.specialization(specialization_id)
);


-- public.offer_specialization definition

-- Drop table

-- DROP TABLE public.offer_specialization;

CREATE TABLE public.offer_specialization (
	offer_id int4 NOT NULL,
	specialization_id int4 NOT NULL,
	CONSTRAINT fk8lohldl4s3tscv3ar1modjcdw FOREIGN KEY (offer_id) REFERENCES public.offer(offer_id),
	CONSTRAINT fkdqlapa30lqgb8d1n7jjpmaajq FOREIGN KEY (specialization_id) REFERENCES public.specialization(specialization_id)
);

-- Inserts for public.category
INSERT INTO public.category (category_name) VALUES
('Engineering'),
('Arts'),
('Science'),
('Business'),
('Education'),
('Health'),
('Technology'),
('Social Sciences'),
('Humanities'),
('Law'),
('Mathematics'),
('Physics'),
('Chemistry'),
('Biology'),
('Information Technology'),
('Nursing'),
('Psychology'),
('Philosophy'),
('Economics'),
('Environmental Studies');

-- Inserts for public.country
INSERT INTO public.country (country_id, country_name) VALUES
('US', 'United States'),
('CA', 'Canada'),
('GB', 'United Kingdom'),
('AU', 'Australia'),
('DE', 'Germany'),
('FR', 'France'),
('IN', 'India'),
('CN', 'China'),
('JP', 'Japan'),
('BR', 'Brazil'),
('ZA', 'South Africa'),
('IT', 'Italy'),
('ES', 'Spain'),
('RU', 'Russia'),
('MX', 'Mexico'),
('KR', 'South Korea'),
('AR', 'Argentina'),
('NL', 'Netherlands'),
('SE', 'Sweden'),
('NO', 'Norway');

-- Inserts for public.course
INSERT INTO public.course (course_credits, course_name, course_semester) VALUES
(3, 'Intro to Engineering', 1),
(4, 'Modern Art History', 1),
(3, 'General Chemistry', 2),
(3, 'Principles of Economics', 2),
(3, 'Calculus I', 1),
(3, 'Introduction to Psychology', 1),
(4, 'Data Structures', 2),
(3, 'Creative Writing', 1),
(3, 'Organic Chemistry', 2),
(3, 'Linear Algebra', 2);

-- Inserts for public.city
INSERT INTO public.city (city_name, country_id) VALUES
('New York', 'US'),
('Toronto', 'CA'),
('London', 'GB'),
('Sydney', 'AU'),
('Berlin', 'DE'),
('Paris', 'FR'),
('Mumbai', 'IN'),
('Beijing', 'CN'),
('Tokyo', 'JP'),
('Sao Paulo', 'BR'),
('Cape Town', 'ZA'),
('Rome', 'IT'),
('Madrid', 'ES'),
('Moscow', 'RU'),
('Mexico City', 'MX'),
('Seoul', 'KR'),
('Buenos Aires', 'AR'),
('Amsterdam', 'NL'),
('Stockholm', 'SE'),
('Oslo', 'NO');

-- Inserts for public.university
INSERT INTO public.university (university_name, city_id) VALUES
('Harvard University', 1),
('University of Toronto', 2),
('University of London', 3),
('University of Sydney', 4),
('Technical University of Berlin', 5),
('Sorbonne University', 6),
('Indian Institute of Technology', 7),
('Peking University', 8),
('University of Tokyo', 9),
('University of Sao Paulo', 10),
('University of Cape Town', 11),
('Sapienza University of Rome', 12),
('Complutense University of Madrid', 13),
('Lomonosov Moscow State University', 14),
('National Autonomous University of Mexico', 15),
('Seoul National University', 16),
('University of Buenos Aires', 17),
('University of Amsterdam', 18),
('Lund University', 19),
('University of Oslo', 20);

-- Inserts for public.offer
INSERT INTO public.offer (description, language, offer_name, end_date, start_date, program_end, program_start, scholarship, receiver_id, sender_id) VALUES
('Full scholarship for the engineering program', 'English', 'Engineering Scholarship', '2025-05-01', '2024-09-01', '2025-01-01', '2024-09-01', 10000, 1, 1),
('Partial scholarship for arts', 'English', 'Arts Program Offer', '2025-05-01', '2024-09-01', '2025-01-01', '2024-09-01', 5000, 2, 2),
('Full scholarship for computer science', 'English', 'CS Scholarship', '2025-05-01', '2024-09-01', '2025-01-01', '2024-09-01', 10000, 3, 3),
('Business program scholarship', 'English', 'Business Offer', '2025-05-01', '2024-09-01', '2025-01-01', '2024-09-01', 3000, 4, 4),
('Health sciences scholarship', 'English', 'Health Sciences Offer', '2025-05-01', '2024-09-01', '2025-01-01', '2024-09-01', 7000, 5, 5);

-- Inserts for public.specialization
INSERT INTO public.specialization (specialization_name, category_id, university_id) VALUES
('Mechanical Engineering', 1, 1),
('Fine Arts', 2, 2),
('Biochemistry', 3, 3),
('Business Administration', 4, 4),
('Nursing', 5, 5),
('Software Engineering', 1, 6),
('Graphic Design', 2, 7),
('Clinical Psychology', 3, 8),
('Environmental Science', 3, 9),
('Data Science', 1, 10);

-- Inserts for public.specialization_course
INSERT INTO public.specialization_course (specialization_id, course_id) VALUES
(1, 1),
(1, 10),
(2, 2),
(2, 8),
(3, 3),
(3, 9),
(4, 4),
(4, 7),
(5, 5),
(5, 6),
(6, 1),
(6, 2),
(7, 3),
(7, 5),
(8, 6),
(8, 9),
(9, 1),
(9, 4),
(10, 2),
(10, 8);

-- Inserts for public.offer_specialization
INSERT INTO public.offer_specialization (offer_id, specialization_id) VALUES
(1, 1),
(2, 2),
(3, 6),
(4, 4),
(5, 5),
(1, 3),
(2, 7),
(3, 8),
(4, 9),
(5, 10);

