CREATE CONSTRAINT ON (node:Game) ASSERT (node.uuid) IS UNIQUE;
CREATE CONSTRAINT ON (node:League) ASSERT (node.uuid) IS UNIQUE;
CREATE CONSTRAINT ON (node:User) ASSERT (node.email) IS UNIQUE;
CREATE CONSTRAINT ON (node:User) ASSERT (node.username) IS UNIQUE;
CREATE CONSTRAINT ON (node:User) ASSERT (node.uuid) IS UNIQUE;