:begin
CREATE CONSTRAINT ON (node:User) ASSERT (node.uuid) IS UNIQUE;
CREATE CONSTRAINT ON (node:Game) ASSERT (node.uuid) IS UNIQUE;
CREATE CONSTRAINT ON (node:League) ASSERT (node.uuid) IS UNIQUE;
CREATE CONSTRAINT ON (node:User) ASSERT (node.username) IS UNIQUE;
CREATE CONSTRAINT ON (node:User) ASSERT (node.email) IS UNIQUE;
CREATE CONSTRAINT ON (node:`UNIQUE IMPORT LABEL`) ASSERT (node.`UNIQUE IMPORT ID`) IS UNIQUE;
:commit
CALL db.awaitIndexes(300);
:begin
UNWIND [{_id:0, properties:{name:"Emil", from:"Sweden", klout:99}}, {_id:1, properties:{learn:"surfing", name:"Johan", from:"Sweden"}}, {_id:2, properties:{name:"Ian", from:"England", title:"author"}}, {_id:3, properties:{name:"Rik", from:"Belgium", pet:"Orval", likes:"pasta"}}, {_id:4, properties:{name:"Allison", from:"California", hobby:"surfing"}}] AS row
MERGE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row._id}) SET n += row.properties SET n:Person;
UNWIND [{uuid:"b3a87485-6d72-4cfb-b079-ed7f40b7e27a", properties:{password:"1234", avatar:1, email:"nekcoj@gmail.com", username:"Jocke"}}, {uuid:"0a5ac6e6-bb45-44ed-9de7-c7675ae47455", properties:{password:"1234", avatar:2, email:"janis@gmail.com", username:"Janis"}}] AS row
MERGE (n:User{uuid: row.uuid}) SET n += row.properties;
UNWIND [{uuid:"33cc4021-cee2-4c28-ba50-01820a793843", properties:{date:1610525880002, pointsToWin:15.0, status:"Finished"}}] AS row
MERGE (n:Game{uuid: row.uuid}) SET n += row.properties;
UNWIND [{uuid:"aefbb4cd-ea7c-4fb0-a385-7813246c8399", properties:{pointsToWin:20, league:"Scania", cardsOnHand:8}}, {uuid:"2ce08334-2fb0-4dd7-821b-739a397cfea6", properties:{pointsToWin:15, league:"Tomtens Verkstad", cardsOnHand:5}}, {uuid:"a1a64d59-a2cb-43c5-9193-bb562097ccff", properties:{pointsToWin:10, league:"ECUtb", cardsOnHand:3}}, {uuid:"d18c85ae-4bc9-49d6-a335-7441cabcead8", properties:{pointsToWin:15, league:"Hoppsan", cardsOnHand:6}}] AS row
MERGE (n:League{uuid: row.uuid}) SET n += row.properties;
:commit
:begin
UNWIND [{start: {uuid:"33cc4021-cee2-4c28-ba50-01820a793843"}, end: {uuid:"2ce08334-2fb0-4dd7-821b-739a397cfea6"}, properties:{}}] AS row
MATCH (start:Game{uuid: row.start.uuid})
MATCH (end:League{uuid: row.end.uuid})
MERGE (start)-[r:IN_LEAGUE]->(end) SET r += row.properties;
UNWIND [{start: {uuid:"b3a87485-6d72-4cfb-b079-ed7f40b7e27a"}, end: {uuid:"33cc4021-cee2-4c28-ba50-01820a793843"}, properties:{winner:0.0, isStartPlayer:true, points:22.0}}, {start: {uuid:"0a5ac6e6-bb45-44ed-9de7-c7675ae47455"}, end: {uuid:"33cc4021-cee2-4c28-ba50-01820a793843"}, properties:{winner:0.0, isStartPlayer:false, points:1.0}}] AS row
MATCH (start:User{uuid: row.start.uuid})
MATCH (end:Game{uuid: row.end.uuid})
MERGE (start)-[r:PLAYED_GAME]->(end) SET r += row.properties;
UNWIND [{start: {_id:0}, end: {_id:1}, properties:{since:2001}}, {start: {_id:0}, end: {_id:2}, properties:{rating:5}}, {start: {_id:1}, end: {_id:2}, properties:{}}, {start: {_id:1}, end: {_id:3}, properties:{}}, {start: {_id:2}, end: {_id:1}, properties:{}}, {start: {_id:2}, end: {_id:4}, properties:{}}, {start: {_id:3}, end: {_id:4}, properties:{}}] AS row
MATCH (start:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row.start._id})
MATCH (end:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row.end._id})
MERGE (start)-[r:KNOWS]->(end) SET r += row.properties;
UNWIND [{start: {uuid:"b3a87485-6d72-4cfb-b079-ed7f40b7e27a"}, end: {uuid:"0a5ac6e6-bb45-44ed-9de7-c7675ae47455"}, properties:{pendingRequest:false}}] AS row
MATCH (start:User{uuid: row.start.uuid})
MATCH (end:User{uuid: row.end.uuid})
MERGE (start)-[r:FRIENDS]->(end) SET r += row.properties;
:commit
:begin
MATCH (n:`UNIQUE IMPORT LABEL`)  WITH n LIMIT 20000 REMOVE n:`UNIQUE IMPORT LABEL` REMOVE n.`UNIQUE IMPORT ID`;
:commit
:begin
DROP CONSTRAINT ON (node:`UNIQUE IMPORT LABEL`) ASSERT (node.`UNIQUE IMPORT ID`) IS UNIQUE;
:commit
