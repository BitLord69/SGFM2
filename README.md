#Super Galaxy Face Melter II - the saga goes online

Medlemmar:<br/>
<ul>
<li>Jan-Erik "Janis" Karlsson - BitLord69</li>
<li>Mathias Permlid - mplovecraft</li>
<li>Alexander Persson - miniharald</li>
<li>Joacim Norbeck - nekcoj</li>
<li>Carl-Johan "Calle" Hornestam - Carljohan-hornestam</li>

## Installation info

### Install Neo4J Community Edition
- https://neo4j.com/download-center/#community
- Connect to Neo4J using http://localhost:7474/browser/
  - Initial credentials are: neo4j/neo4j
  - After logging in it'll ask you to set a new password
- Make sure you have neo4j in your services.msc (systemctl) and have it autostart on boot

### Install Neo4J plugin
- Download https://github.com/neo4j-contrib/neo4j-apoc-procedures/releases/download/4.0.0.12/apoc-4.0.0.12-all.jar
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

### Copy and paste this in the Neo browser
    ```

      CREATE CONSTRAINT ON (user:User)
      ASSERT user.uuid IS UNIQUE
      CREATE CONSTRAINT ON (game:Game)
      ASSERT game.uuid IS UNIQUE
      CREATE CONSTRAINT ON (league:League)
      ASSERT league.uuid IS UNIQUE

      CALL apoc.uuid.install('User')
      YIELD label, installed
      RETURN label, installed
      CALL apoc.uuid.install('Game')
      YIELD label, installed
      RETURN label, installed
      CALL apoc.uuid.install('League')
      YIELD label, installed
      RETURN label, installed
    ```
