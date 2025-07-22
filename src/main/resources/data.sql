-- Insert default users when application starts (only if they don't exist)
-- Note: Passwords should be encoded using BCrypt in a real application

INSERT INTO user (
    username, 
    password, 
    email, 
    first_name, 
    last_name, 
    role, 
    profile_image_url, 
    phone_number, 
    address, 
    bio, 
    created_at, 
    last_login, 
    email_verified
)
SELECT * FROM (
    SELECT 
        'saikiran53' as username,
        '$2a$12$6QerDr827N4HfjZEXPmogeAvHkERcz0ox4Q5D10UoqWHC3KfsIDL.' as password,
        'saikiran@gmail.com' as email,
        'Saikiran' as first_name,
        'Kammari' as last_name,
        'ADMIN' as role,
        NULL as profile_image_url,
        '9876543210' as phone_number,
        'Plot No 123, Krishna Nagar, Vijayawada, Andhra Pradesh 520008' as address,
        'System Administrator with 8+ years experience in Java and Spring Boot development. Passionate about building scalable applications.' as bio,
        CURRENT_TIMESTAMP as created_at,
        NULL as last_login,
        true as email_verified
) AS tmp
WHERE NOT EXISTS (
    SELECT email FROM user WHERE email = 'saikiran@gmail.com'
);

INSERT INTO user (
    username, 
    password, 
    email, 
    first_name, 
    last_name, 
    role, 
    profile_image_url, 
    phone_number, 
    address, 
    bio, 
    created_at, 
    last_login, 
    email_verified
)
SELECT * FROM (
    SELECT 
        'admin' as username,
        '$2a$12$6QerDr827N4HfjZEXPmogeAvHkERcz0ox4Q5D10UoqWHC3KfsIDL.' as password,
        'admin@gmail.com' as email,
        'Admin' as first_name,
        'User' as last_name,
        'ADMIN' as role,
        NULL as profile_image_url,
        '8765432109' as phone_number,
        'H.No 45-67-89, Banjara Hills, Hyderabad, Telangana 500034' as address,
        'Primary administrator account for system management and user oversight.' as bio,
        CURRENT_TIMESTAMP as created_at,
        NULL as last_login,
        true as email_verified
) AS tmp
WHERE NOT EXISTS (
    SELECT email FROM user WHERE email = 'admin@gmail.com'
);

INSERT INTO user (username, password, email, first_name, last_name, role, profile_image_url, phone_number, address, bio, created_at, last_login, email_verified)
SELECT * FROM (SELECT 'ramesh.reddy' as username, '$2b$12$R9h/cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUW' as password, 'ramesh.reddy@email.com' as email, 'Ramesh' as first_name, 'Reddy' as last_name, 'USER' as role, NULL as profile_image_url, '9123456789' as phone_number, 'Door No 12-34-56, Gachibowli, Hyderabad, Telangana 500032' as address, 'Full Stack Developer with expertise in React and Node.js. Love working on innovative tech solutions.' as bio, CURRENT_TIMESTAMP as created_at, NULL as last_login, true as email_verified) AS tmp WHERE NOT EXISTS (SELECT email FROM user WHERE email = 'ramesh.reddy@email.com');

INSERT INTO user (username, password, email, first_name, last_name, role, profile_image_url, phone_number, address, bio, created_at, last_login, email_verified)
SELECT * FROM (SELECT 'priya.sharma' as username, '$2b$12$R9h/cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUW' as password, 'priya.sharma@email.com' as email, 'Priya' as first_name, 'Sharma' as last_name, 'USER' as role, NULL as profile_image_url, '9234567890' as phone_number, 'Flat 301, Green Valley Apartments, Vizag, Andhra Pradesh 530017' as address, 'Frontend Developer specializing in Angular and Vue.js. Passionate about creating beautiful user interfaces.' as bio, CURRENT_TIMESTAMP as created_at, NULL as last_login, true as email_verified) AS tmp WHERE NOT EXISTS (SELECT email FROM user WHERE email = 'priya.sharma@email.com');

INSERT INTO user (username, password, email, first_name, last_name, role, profile_image_url, phone_number, address, bio, created_at, last_login, email_verified)
SELECT * FROM (SELECT 'krishna.rao' as username, '$2b$12$R9h/cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUW' as password, 'krishna.rao@email.com' as email, 'Krishna' as first_name, 'Rao' as last_name, 'USER' as role, NULL as profile_image_url, '9345678901' as phone_number, 'House No 789, Madhapur, Hyderabad, Telangana 500081' as address, 'Backend Developer working with Spring Boot and microservices architecture. Enjoy solving complex problems.' as bio, CURRENT_TIMESTAMP as created_at, NULL as last_login, true as email_verified) AS tmp WHERE NOT EXISTS (SELECT email FROM user WHERE email = 'krishna.rao@email.com');

INSERT INTO user (username, password, email, first_name, last_name, role, profile_image_url, phone_number, address, bio, created_at, last_login, email_verified)
SELECT * FROM (SELECT 'lakshmi.devi' as username, '$2b$12$R9h/cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUW' as password, 'lakshmi.devi@email.com' as email, 'Lakshmi' as first_name, 'Devi' as last_name, 'USER' as role, NULL as profile_image_url, '9456789012' as phone_number, 'Plot 456, Kukatpally, Hyderabad, Telangana 500072' as address, 'QA Engineer with 5+ years experience in automation testing. Detail-oriented and quality-focused professional.' as bio, CURRENT_TIMESTAMP as created_at, NULL as last_login, true as email_verified) AS tmp WHERE NOT EXISTS (SELECT email FROM user WHERE email = 'lakshmi.devi@email.com');

INSERT INTO user (username, password, email, first_name, last_name, role, profile_image_url, phone_number, address, bio, created_at, last_login, email_verified)
SELECT * FROM (SELECT 'vijay.kumar' as username, '$2b$12$R9h/cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUW' as password, 'vijay.kumar@email.com' as email, 'Vijay' as first_name, 'Kumar' as last_name, 'ADMIN' as role, NULL as profile_image_url, '9567890123' as phone_number, 'Door No 23-45-67, Guntur, Andhra Pradesh 522001' as address, 'DevOps Engineer specializing in AWS and Kubernetes. Leading cloud migration projects.' as bio, CURRENT_TIMESTAMP as created_at, NULL as last_login, true as email_verified) AS tmp WHERE NOT EXISTS (SELECT email FROM user WHERE email = 'vijay.kumar@email.com');

INSERT INTO user (username, password, email, first_name, last_name, role, profile_image_url, phone_number, address, bio, created_at, last_login, email_verified)
SELECT * FROM (SELECT 'anitha.nair' as username, '$2b$12$R9h/cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUW' as password, 'anitha.nair@email.com' as email, 'Anitha' as first_name, 'Nair' as last_name, 'USER' as role, NULL as profile_image_url, '9678901234' as phone_number, 'Flat 501, Cyber Towers, Hitec City, Hyderabad, Telangana 500081' as address, 'Data Analyst with expertise in Python and machine learning. Love turning data into insights.' as bio, CURRENT_TIMESTAMP as created_at, NULL as last_login, true as email_verified) AS tmp WHERE NOT EXISTS (SELECT email FROM user WHERE email = 'anitha.nair@email.com');

INSERT INTO user (username, password, email, first_name, last_name, role, profile_image_url, phone_number, address, bio, created_at, last_login, email_verified)
SELECT * FROM (SELECT 'suresh.babu' as username, '$2b$12$R9h/cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUW' as password, 'suresh.babu@email.com' as email, 'Suresh' as first_name, 'Babu' as last_name, 'USER' as role, NULL as profile_image_url, '9789012345' as phone_number, 'H.No 34-56-78, Tirupati, Andhra Pradesh 517501' as address, 'Mobile App Developer working with Flutter and React Native. Building cross-platform solutions.' as bio, CURRENT_TIMESTAMP as created_at, NULL as last_login, true as email_verified) AS tmp WHERE NOT EXISTS (SELECT email FROM user WHERE email = 'suresh.babu@email.com');

INSERT INTO user (username, password, email, first_name, last_name, role, profile_image_url, phone_number, address, bio, created_at, last_login, email_verified)
SELECT * FROM (SELECT 'kavya.reddy' as username, '$2b$12$R9h/cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUW' as password, 'kavya.reddy@email.com' as email, 'Kavya' as first_name, 'Reddy' as last_name, 'USER' as role, NULL as profile_image_url, '9890123456' as phone_number, 'Apartment 201, Jubilee Hills, Hyderabad, Telangana 500033' as address, 'UI/UX Designer with a keen eye for modern design trends. Creating intuitive and beautiful user experiences.' as bio, CURRENT_TIMESTAMP as created_at, NULL as last_login, true as email_verified) AS tmp WHERE NOT EXISTS (SELECT email FROM user WHERE email = 'kavya.reddy@email.com');

INSERT INTO user (username, password, email, first_name, last_name, role, profile_image_url, phone_number, address, bio, created_at, last_login, email_verified)
SELECT * FROM (SELECT 'ravi.teja' as username, '$2b$12$R9h/cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUW' as password, 'ravi.teja@email.com' as email, 'Ravi' as first_name, 'Teja' as last_name, 'USER' as role, NULL as profile_image_url, '9012345678' as phone_number, 'Plot No 67-89-12, Kurnool, Andhra Pradesh 518001' as address, 'Cybersecurity Specialist focused on application security and penetration testing. Protecting digital assets.' as bio, CURRENT_TIMESTAMP as created_at, NULL as last_login, true as email_verified) AS tmp WHERE NOT EXISTS (SELECT email FROM user WHERE email = 'ravi.teja@email.com');

INSERT INTO user (username, password, email, first_name, last_name, role, profile_image_url, phone_number, address, bio, created_at, last_login, email_verified)
SELECT * FROM (SELECT 'meera.krishna' as username, '$2b$12$R9h/cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUW' as password, 'meera.krishna@email.com' as email, 'Meera' as first_name, 'Krishna' as last_name, 'ADMIN' as role, NULL as profile_image_url, '8901234567' as phone_number, 'Door No 45-67-89, Kakinada, Andhra Pradesh 533001' as address, 'Project Manager with PMP certification. Leading agile development teams and delivering successful projects.' as bio, CURRENT_TIMESTAMP as created_at, NULL as last_login, true as email_verified) AS tmp WHERE NOT EXISTS (SELECT email FROM user WHERE email = 'meera.krishna@email.com');