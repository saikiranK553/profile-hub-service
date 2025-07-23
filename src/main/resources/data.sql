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