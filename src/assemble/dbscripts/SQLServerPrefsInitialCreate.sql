-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: changelog.groovy
-- Ran at: 12/21/12 4:22 PM

-- Liquibase version: 2.0.1
-- *********************************************************************

-- Create Database Lock Table
CREATE TABLE [dbo].[DATABASECHANGELOGLOCK] ([ID] INT NOT NULL, [LOCKED] BIT NOT NULL, [LOCKGRANTED] DATETIME, [LOCKEDBY] VARCHAR(255), CONSTRAINT [PK_DATABASECHANGELOGLOCK] PRIMARY KEY ([ID]))
GO

INSERT INTO [dbo].[DATABASECHANGELOGLOCK] ([ID], [LOCKED]) VALUES (1, 0)
GO

-- Lock Database
-- Create Database Change Log Table
CREATE TABLE [dbo].[DATABASECHANGELOG] ([ID] VARCHAR(63) NOT NULL, [AUTHOR] VARCHAR(63) NOT NULL, [FILENAME] VARCHAR(200) NOT NULL, [DATEEXECUTED] DATETIME NOT NULL, [ORDEREXECUTED] INT NOT NULL, [EXECTYPE] VARCHAR(10) NOT NULL, [MD5SUM] VARCHAR(35), [DESCRIPTION] VARCHAR(255), [COMMENTS] VARCHAR(255), [TAG] VARCHAR(255), [LIQUIBASE] VARCHAR(20), CONSTRAINT [PK_DATABASECHANGELOG] PRIMARY KEY ([ID], [AUTHOR], [FILENAME]))
GO

-- Changeset changelog_5.0.0.groovy::5.0.0-1::metric::(Checksum: 3:d0ed4813812e9adb341ba2d8e50427da)
-- Creating metric table
CREATE TABLE [dbo].[metric] ([id] BIGINT IDENTITY  NOT NULL, [version] BIGINT NOT NULL, [component] VARCHAR(200) NOT NULL, [component_id] VARCHAR(255) NOT NULL, [instance_id] VARCHAR(255) NOT NULL, [metric_time] VARCHAR(255) NOT NULL, [metric_type_id] VARCHAR(255) NOT NULL, [site] VARCHAR(255) NOT NULL, [user_agent] VARCHAR(255) NOT NULL, [user_id] VARCHAR(255) NOT NULL, [user_name] VARCHAR(255) NOT NULL, [widget_data] VARCHAR(255), CONSTRAINT [metricPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('metric', 'Creating metric table', GETDATE(), 'Create Table', 'EXECUTED', 'changelog_5.0.0.groovy', '5.0.0-1', '2.0.1', '3:d0ed4813812e9adb341ba2d8e50427da', 1)
GO

-- Changeset changelog_5.0.0.groovy::5.0.0-2::metric::(Checksum: 3:fc5ad69bb5039167b4172b1dc69e9fc3)
CREATE TABLE [dbo].[owf_group] ([id] BIGINT IDENTITY  NOT NULL, [version] BIGINT NOT NULL, [automatic] BIT NOT NULL, [description] VARCHAR(255), [email] VARCHAR(255), [name] VARCHAR(200) NOT NULL, [status] VARCHAR(8) NOT NULL, CONSTRAINT [owf_groupPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('metric', '', GETDATE(), 'Create Table', 'EXECUTED', 'changelog_5.0.0.groovy', '5.0.0-2', '2.0.1', '3:fc5ad69bb5039167b4172b1dc69e9fc3', 2)
GO

-- Changeset changelog_5.0.0.groovy::5.0.0-3::metric::(Checksum: 3:776f3e5531c86c55dd34757827469ecf)
CREATE TABLE [dbo].[owf_group_people] ([group_id] BIGINT NOT NULL, [person_id] BIGINT NOT NULL)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('metric', '', GETDATE(), 'Create Table', 'EXECUTED', 'changelog_5.0.0.groovy', '5.0.0-3', '2.0.1', '3:776f3e5531c86c55dd34757827469ecf', 3)
GO

-- Changeset changelog_5.0.0.groovy::5.0.0-4::metric::(Checksum: 3:2c576dc94d7a28be9508a8ed6ff861ac)
CREATE TABLE [dbo].[person] ([id] BIGINT IDENTITY  NOT NULL, [version] BIGINT NOT NULL, [description] VARCHAR(255), [email] VARCHAR(255), [email_show] BIT NOT NULL, [enabled] BIT NOT NULL, [last_login] DATETIME, [prev_login] DATETIME, [user_real_name] VARCHAR(200) NOT NULL, [username] VARCHAR(200) NOT NULL, CONSTRAINT [personPK] PRIMARY KEY ([id]), UNIQUE ([username]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('metric', '', GETDATE(), 'Create Table', 'EXECUTED', 'changelog_5.0.0.groovy', '5.0.0-4', '2.0.1', '3:2c576dc94d7a28be9508a8ed6ff861ac', 4)
GO

-- Changeset changelog_5.0.0.groovy::5.0.0-5::metric::(Checksum: 3:8e1b2277e0aa08f40ef04493d798b010)
CREATE TABLE [dbo].[role] ([id] BIGINT IDENTITY  NOT NULL, [version] BIGINT NOT NULL, [authority] VARCHAR(255) NOT NULL, [description] VARCHAR(255) NOT NULL, CONSTRAINT [rolePK] PRIMARY KEY ([id]), UNIQUE ([authority]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('metric', '', GETDATE(), 'Create Table', 'EXECUTED', 'changelog_5.0.0.groovy', '5.0.0-5', '2.0.1', '3:8e1b2277e0aa08f40ef04493d798b010', 5)
GO

-- Changeset changelog_5.0.0.groovy::5.0.0-6::metric::(Checksum: 3:e33bfaaeab763096aa90a8483a2e2127)
CREATE TABLE [dbo].[role_people] ([role_id] BIGINT NOT NULL, [person_id] BIGINT NOT NULL)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('metric', '', GETDATE(), 'Create Table', 'EXECUTED', 'changelog_5.0.0.groovy', '5.0.0-6', '2.0.1', '3:e33bfaaeab763096aa90a8483a2e2127', 6)
GO

-- Changeset changelog_5.0.0.groovy::5.0.0-7::metric::(Checksum: 3:80e84375f463faef757e5184c1a8c11d)
ALTER TABLE [dbo].[owf_group_people] ADD PRIMARY KEY ([group_id], [person_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('metric', '', GETDATE(), 'Add Primary Key', 'EXECUTED', 'changelog_5.0.0.groovy', '5.0.0-7', '2.0.1', '3:80e84375f463faef757e5184c1a8c11d', 7)
GO

-- Changeset changelog_5.0.0.groovy::5.0.0-8::metric::(Checksum: 3:d5d8393560daec118b4ec2ee8fa2c369)
ALTER TABLE [dbo].[role_people] ADD PRIMARY KEY ([role_id], [person_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('metric', '', GETDATE(), 'Add Primary Key', 'EXECUTED', 'changelog_5.0.0.groovy', '5.0.0-8', '2.0.1', '3:d5d8393560daec118b4ec2ee8fa2c369', 8)
GO

-- Changeset changelog_5.0.0.groovy::5.0.0-9::metric::(Checksum: 3:ccbd0718a11721db8399e3cb503f4728)
CREATE UNIQUE INDEX [username_unique_1334248717317] ON [dbo].[person]([username])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('metric', '', GETDATE(), 'Create Index', 'EXECUTED', 'changelog_5.0.0.groovy', '5.0.0-9', '2.0.1', '3:ccbd0718a11721db8399e3cb503f4728', 9)
GO

-- Changeset changelog_5.0.0.groovy::5.0.0-10::metric::(Checksum: 3:e71e770a683270a84881275f7e5b84f1)
CREATE UNIQUE INDEX [authority_unique_1334248717321] ON [dbo].[role]([authority])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('metric', '', GETDATE(), 'Create Index', 'EXECUTED', 'changelog_5.0.0.groovy', '5.0.0-10', '2.0.1', '3:e71e770a683270a84881275f7e5b84f1', 10)
GO

-- Changeset changelog_5.0.0.groovy::5.0.0-11::metric::(Checksum: 3:4d31aea5bd1733c9e178001830e77e67)
ALTER TABLE [dbo].[owf_group_people] ADD CONSTRAINT [FK28113703B197B21] FOREIGN KEY ([group_id]) REFERENCES [dbo].[owf_group] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('metric', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', 'changelog_5.0.0.groovy', '5.0.0-11', '2.0.1', '3:4d31aea5bd1733c9e178001830e77e67', 11)
GO

-- Changeset changelog_5.0.0.groovy::5.0.0-12::metric::(Checksum: 3:d67714cb20092581a9533ccf13321f44)
ALTER TABLE [dbo].[owf_group_people] ADD CONSTRAINT [FK2811370C1F5E0B3] FOREIGN KEY ([person_id]) REFERENCES [dbo].[person] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('metric', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', 'changelog_5.0.0.groovy', '5.0.0-12', '2.0.1', '3:d67714cb20092581a9533ccf13321f44', 12)
GO

-- Changeset changelog_5.0.0.groovy::5.0.0-13::metric::(Checksum: 3:8dd1b72247c987acb86dd51984dd500d)
ALTER TABLE [dbo].[role_people] ADD CONSTRAINT [FK28B75E78C1F5E0B3] FOREIGN KEY ([person_id]) REFERENCES [dbo].[person] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('metric', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', 'changelog_5.0.0.groovy', '5.0.0-13', '2.0.1', '3:8dd1b72247c987acb86dd51984dd500d', 13)
GO

-- Changeset changelog_5.0.0.groovy::5.0.0-14::metric::(Checksum: 3:dc986d30fe40d96c2e4c1d479a1b9d20)
ALTER TABLE [dbo].[role_people] ADD CONSTRAINT [FK28B75E7870B353] FOREIGN KEY ([role_id]) REFERENCES [dbo].[role] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('metric', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', 'changelog_5.0.0.groovy', '5.0.0-14', '2.0.1', '3:dc986d30fe40d96c2e4c1d479a1b9d20', 14)
GO

