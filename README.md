# Super Galaxy Face Melter II - the saga goes online

Medlemmar:<br/>

<ul>
<li>Joacim Norbeck - nekcoj</li>
<li>Alexander Persson - miniharald</li>
<li>Jan-Erik "Janis" Karlsson - BitLord69</li>
<li>Carl-Johan "Calle" Hornestam - Carljohan-hornestam</li>
<li>Mathias Permlid - mplovecraft: <strong>tyvärr utgått ur gruppen</strong></li>

## Installation info

### Install Neo4J Community Edition

- https://neo4j.com/download-center/#community
- Connect to Neo4J using http://localhost:7474/browser/
  - Initial credentials are: neo4j/neo4j
  - After logging in it'll ask you to set a new password
- Make sure you have neo4j in your services.msc (systemctl) and have it autostart on boot
  - https://neo4j.com/docs/operations-manual/current/installation/windows/

### Install Neo4J plugin

- Download https://github.com/neo4j-contrib/neo4j-apoc-procedures/releases/download/4.2.0.0/apoc-4.2.0.0-all.jar
- Place it in your `Neo4Jinstallfolder/plugins` folder
- Add the following to your `Neo4Jinstallfolder/conf/neo4j.conf`
  ```
  dbms.directories.plugins=plugins
  # EDIT the import-directory to your relevant filepath
  dbms.directories.import=G:\\servicefy\\backend\\modules
  dbms.security.procedures.whitelist=apoc.export.*,apoc.uuid.*,apoc.periodic.iterate,apoc.cypher.runFile, apoc.create.uuid, apoc.path.subgraphAll
  apoc.import.file.enabled=true
  apoc.export.file.enabled=true
  apoc.uuid.enabled=true
  ```
- Make sure `dbms.directories.import` etc are not overriden by the defaults (duplicate entries)
- Changing your config-file requires you to restart the neo4j-service
- In the neo4j console, add this constraint
  ```
  CREATE CONSTRAINT ON (category:Category)
  ASSERT category.uuid IS UNIQUE
  ```
  and add the uuid
  ```
  CALL apoc.uuid.install('Category')
  YIELD label, installed
  RETURN label, installed
  ```

### Install Redis (in-memory database, used for Sessions)

- Windows: https://github.com/tporadowski/redis/releases (Current version: 5.0.9 msi)
- Unix: https://redis.io/download
- Make sure you have redis in your services.msc (systemctl) and have it autostart on boot

### Passwords etc

- Copy the file `backend/database-server/secrets.copy-me.json` and rename it to `secrets.json`
- Open the file `backend/database-server/secrets.json`
  - Insert your neo4j credentials
  - Generate a random 60-character string (google string generator) and insert it in `session_secrets`
- Note! The secrets.json is <strong><em>NOT</em></strong> included in the repo on github!

---

## Run

These will install/start both the backend and the frontend using custom npm-scripts.

- `npm install`
- `npm start`

---

## Miscellaneous

### How to view Neo4J content

- Go to http://localhost:7474/browser/
- Do the tutorials displayed on the startscreen
