-- When project starts, Spring will create table called SEC_USER
-- There are 4 attributes, creating 4 columns in the SEC_USER table
-- userID cannot be null, we want to be able to search this DB table efficiently, this means we need a Primary Key.  
create table SEC_USER
(
  userID           BIGINT NOT NULL Primary Key AUTO_INCREMENT,
  userName         VARCHAR(36) NOT NULL UNIQUE,
  encryptedPassword VARCHAR(128) NOT NULL,
  ENABLED           BIT NOT NULL 
) ;

-- Since we can have multiple roles, we will create linking table between userID and roleID
-- Need to set up foreign keys to link tables
-- Here, our goal is to match roleName to roleId
create table SEC_ROLE
(
  roleID   BIGINT NOT NULL Primary Key AUTO_INCREMENT,
  roleName VARCHAR(30) NOT NULL UNIQUE
) ;


create table USER_ROLE
(
  ID      BIGINT NOT NULL Primary Key AUTO_INCREMENT,
  userID BIGINT NOT NULL,
  roleID BIGINT NOT NULL
);

alter table USER_ROLE
  add constraint USER_ROLE_UK unique (userID, roleID);

alter table USER_ROLE
  add constraint USER_ROLE_FK1 foreign key (userID)
  references SEC_USER (userID);
 
alter table USER_ROLE
  add constraint USER_ROLE_FK2 foreign key (roleID)
  references SEC_ROLE (roleID);

--insert into SEC_User (userName, encryptedPassword, ENABLED)
--values ('TomBrady', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 1);
-- 
insert into SEC_User (userName, encryptedPassword, ENABLED)
values ('admin', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 1);
 
insert into SEC_User (userName, encryptedPassword, ENABLED)
values ('a', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 1);

-- There are 3 roles. Always use prefix "ROLE_"
-- SpringBoot uses the prefix "ROLE_" so we must follow it!
-- Roles should be uppercase as well
insert into sec_role (roleName)
values ('ROLE_ADMIN');
 
insert into sec_role (roleName)
values ('ROLE_MEMBER');

--insert into sec_role (roleName)
--values ('ROLE_STUDENT');
 

-- Here, we are saying that the user who's userID is 1, 
-- has a roleID of 1 and is an admin 
-- and is also a user, becaus they also have roleID of 2.

insert into user_role (userID, roleID)
values (1, 1);
 
--insert into user_role (userID, roleID)
--values (1, 2);
 
 -- userID who has userID of 2, is just a user.
insert into user_role (userID, roleID)
values (2, 2);


-- userID 3 here is showing they have roleID of 1,2,3 which indicates they have role of ADMIN, USER,
-- and STUDENT (sketchy..)
--insert into user_role (userID, roleID)
--values (3, 1);
-- 
--insert into user_role (userID, roleID)
--values (3, 2);
--
--insert into user_role (userID, roleID)
--values (3, 3);

COMMIT;

