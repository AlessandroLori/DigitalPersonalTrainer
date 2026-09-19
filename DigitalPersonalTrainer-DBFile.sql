-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`GeneralUser`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`GeneralUser` (
  `Username` VARCHAR(20) NOT NULL,
  `Pass` VARCHAR(50) NOT NULL,
  `Tipo` ENUM('pt', 'utente', 'segreteria') NOT NULL,
  PRIMARY KEY (`Username`),
  UNIQUE INDEX `Username_UNIQUE` (`Username` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`PersonalTrainer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`PersonalTrainer` (
  `CF` CHAR(16) NOT NULL,
  `Nome` VARCHAR(50) NOT NULL,
  `Cognome` VARCHAR(50) NOT NULL,
  `Username` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`CF`),
  INDEX `Username_idx` (`Username` ASC) VISIBLE,
  CONSTRAINT `Username`
    FOREIGN KEY (`Username`)
    REFERENCES `mydb`.`GeneralUser` (`Username`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Utente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Utente` (
  `CF` CHAR(16) NOT NULL,
  `Nome` VARCHAR(45) NOT NULL,
  `Cognome` VARCHAR(45) NOT NULL,
  `PersonalTrainer` CHAR(16) NOT NULL,
  `Username` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`CF`),
  INDEX `PersonalTrainer_idx` (`PersonalTrainer` ASC) VISIBLE,
  INDEX `Username_idx` (`Username` ASC) VISIBLE,
  CONSTRAINT `PersonalTrainer`
    FOREIGN KEY (`PersonalTrainer`)
    REFERENCES `mydb`.`PersonalTrainer` (`CF`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_username_utente`
    FOREIGN KEY (`Username`)
    REFERENCES `mydb`.`GeneralUser` (`Username`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Scheda`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Scheda` (
  `DataEmissione` DATE NOT NULL,
  `DataScad` DATE NULL,
  `Tipo` ENUM('scaduta', 'attiva') NOT NULL,
  `Utente` CHAR(16) NOT NULL,
  PRIMARY KEY (`DataEmissione`, `Utente`),
  INDEX `Cliente_idx` (`Utente` ASC) VISIBLE,
  CONSTRAINT `fk_scheda_UtenteCF`
    FOREIGN KEY (`Utente`)
    REFERENCES `mydb`.`Utente` (`CF`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Esercizio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Esercizio` (
  `Nome` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`Nome`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Composta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Composta` (
  `DataEmissioneScheda` DATE NOT NULL,
  `NumeroSerie` INT NOT NULL,
  `NumeroRipetizioni` INT NOT NULL,
  `Indice` INT NOT NULL,
  `Macchinario` VARCHAR(50) NOT NULL,
  `Utente` CHAR(16) NOT NULL,
  `NomeEsercizio` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`DataEmissioneScheda`, `Utente`, `Macchinario`),
  INDEX `DataEmissioneScheda+Cliente_idx` (`DataEmissioneScheda` ASC, `Utente` ASC) VISIBLE,
  INDEX `NomeMacchinario_idx` (`Macchinario` ASC) VISIBLE,
  CONSTRAINT `DataEmissioneScheda+Cliente`
    FOREIGN KEY (`DataEmissioneScheda` , `Utente`)
    REFERENCES `mydb`.`Scheda` (`DataEmissione` , `Utente`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `NomeMacchinario`
    FOREIGN KEY (`Macchinario`)
    REFERENCES `mydb`.`Esercizio` (`Nome`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`SessioneAllenamento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`SessioneAllenamento` (
  `DataAllenamento` DATE NOT NULL,
  `OrarioInizio` TIME NOT NULL,
  `OrarioFine` TIME NULL,
  `Utente` CHAR(16) NOT NULL,
  `DataEmiss` DATE NOT NULL,
  PRIMARY KEY (`DataAllenamento`, `Utente`, `DataEmiss`),
  INDEX `Utente_idx` (`Utente` ASC, `DataEmiss` ASC) VISIBLE,
  CONSTRAINT `fk_DataAllen_UtenteScheda`
    FOREIGN KEY (`Utente` , `DataEmiss`)
    REFERENCES `mydb`.`Scheda` (`Utente` , `DataEmissione`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Contrassegna`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Contrassegna` (
  `Nome` VARCHAR(50) NOT NULL,
  `DataAllen` DATE NOT NULL,
  `SerieCorrente` INT NOT NULL,
  `Utente` CHAR(16) NOT NULL,
  `DataEmissScheda` DATE NOT NULL,
  PRIMARY KEY (`Nome`, `DataAllen`, `Utente`, `DataEmissScheda`),
  INDEX `Data_idx` (`DataAllen` ASC, `Utente` ASC, `DataEmissScheda` ASC) VISIBLE,
  CONSTRAINT `Nome`
    FOREIGN KEY (`Nome`)
    REFERENCES `mydb`.`Esercizio` (`Nome`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `Data+Utente+DataScheda`
    FOREIGN KEY (`DataAllen` , `Utente` , `DataEmissScheda`)
    REFERENCES `mydb`.`SessioneAllenamento` (`DataAllenamento` , `Utente` , `DataEmiss`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `mydb` ;

-- -----------------------------------------------------
-- procedure aggiungi_utente
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `aggiungi_utente` (
	in cf char(16),
    in nome varchar(50),
    in cognome varchar(50),
    in username varchar(20),
    in passwrd varchar(50) )
BEGIN
	
    declare ptcf varchar(20);
    declare exit handler for sqlexception
    BEGIN
		rollback; -- annulla la transazione
        resignal; -- ritorna il segnale al client
	END;
    
    if(mydb.cf_check(cf) is FALSE) then
		signal sqlstate '45056'set message_text= 'CODICE FISCALE ERRATO';
	END if;
    
    set transaction isolation level READ COMMITTED; -- read committed perché unrepetable read è ok in questo caso
    start transaction;
    
		-- Seleziono PersonalTrainer con meno utenti e aggiungo nuovo utente al generaluser con il suo tipo
    
		select PersonalTrainer.CF into ptcf
        from PersonalTrainer left join Utente on PersonalTrainer.CF = Utente.PersonalTrainer
        group by PersonalTrainer.CF
        order by count(Utente.PersonalTrainer) ASC
        limit 1;
        
        insert into `GeneralUser` (`Username`, `Pass`, `Tipo`)
        values (username, sha1(passwrd), 'utente');
        
        -- Aggiungo utente all'entità
        
        insert into `Utente` (`CF`, `Nome`, `Cognome`, `Username`, `PersonalTrainer`)
        values(cf, nome, cognome, username, ptcf);
        
        COMMIT;
        
END$$

DELIMITER ;

-- -----------------------------------------------------
-- function cf_check
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE FUNCTION `cf_check` (CF char(16)) returns boolean

	deterministic

BEGIN

	if CF regexp '^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$' then
		return TRUE;
	else
		return FALSE;
	end if;
    
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure aggiungi_personaltrainer
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `aggiungi_personaltrainer` (
	in cf char(16),
    in nome varchar(50),
    in cognome varchar(50),
    in username varchar(20),
	in passwrd varchar(50) )
BEGIN
    
    declare exit handler for sqlexception
    BEGIN
		rollback; -- annulla la transazione
        resignal; -- ritorna il segnale al client
	END;
    
    if(mydb.cf_check(cf) is FALSE) then
		signal sqlstate '45001'set message_text= 'CODICE FISCALE ERRATO';
	END if;
    
    set transaction isolation level READ COMMITTED; -- read committed perché unrepetable read è ok in questo caso
    start transaction;
    
		-- Aggiungo alla tabella del generaluser settando il tipi e di seguito alla tabella dei PT
		
		insert into `GeneralUser` (`Username`, `pass`, `tipo`)
        values (username, sha1(passwrd), 'pt');
        
		insert into `PersonalTrainer` (`CF`, `Nome`, `Cognome`, `Username`)
        values (cf, nome, cognome, username);
    
		COMMIT;    

END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure aggiungi_esercizio
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `aggiungi_esercizio` (
	in nome varchar(50) )
BEGIN	
    insert into `Esercizio` (`Nome`)
    values (nome);
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure inizio_allenamento
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `inizio_allenamento` (
	in cfuser char(16) )
BEGIN

	declare dataemissione date;
    declare giorno date;
    declare ora time;
    
    declare exit handler for sqlexception
    BEGIN
		rollback; -- annulla la transazione
		resignal; -- ritorna il segnale al client
	 END;
     
	set transaction isolation level READ COMMITTED; 
    start transaction;
    
		set giorno = curdate();
		set ora = curtime();
    
		if(mydb.cf_check(cfuser) is FALSE) then
			signal sqlstate '45003'set message_text= 'CODICE FISCALE ERRATO';
		END if;
    

		select Scheda.DataEmissione into dataemissione
        from Scheda
        where DataScad is null and Utente = cfuser;
	
		insert into `SessioneAllenamento` ( `DataAllenamento`, `OrarioInizio`, `OrarioFine`, `Utente`, `DataEmiss`)
        values (giorno, ora, null, cfuser, dataemissione);
        
        COMMIT;

END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure fine_allenamento
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `fine_allenamento` (
	in cfuser char(16)

)
BEGIN
	
    declare dataallenamento date;
    declare ora time;

	declare exit handler for sqlexception
    BEGIN
		rollback; -- annulla la transazione
        resignal; -- ritorna il segnale al client
	END;
    
    if(mydb.cf_check(cfuser) is FALSE) then
		signal sqlstate '45004'set message_text= 'CODICE FISCALE ERRATO';
	END if;
    
    set ora = curtime();
    
    set transaction isolation level READ COMMITTED;
	start transaction;
    
		update SessioneAllenamento
        set OrarioFine = ora
        where cfuser= Utente and OrarioFine is null;

		COMMIT;

END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure crea_scheda
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `crea_scheda` (
	in data_emissione date,
    in cfuser char(16), 
    in cfpt char(16) )
BEGIN
	
	declare tempcfpt char(16);
        
	declare exit handler for sqlexception
    BEGIN
		rollback; -- annulla la transazione
        resignal; -- ritorna il segnale al client
	END;
    
    if((mydb.cf_check(cfuser) is FALSE) OR mydb.cf_check(cfpt)is FALSE) then
		signal sqlstate '45006'set message_text= 'CODICE FISCALE ERRATO';
	END if;

	set transaction isolation level READ COMMITTED;
    start transaction;
    
		select PersonalTrainer into tempcfpt
        from Utente
        where CF= cfuser;
        
        if(tempcfpt <> cfpt) then
			signal sqlstate '45007'set message_text= 'CLIENTE NON ASSOCIATO A QUESTO PERSONAL TRAINER';
		END if;


		insert into `Scheda`(`DataEmissione`, `DataScad`, `Tipo`, `Utente`)
        values (data_emissione, NULL, 'attiva', cfuser);

		COMMIT;

END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure aggiungi_scheda_esercizio
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `aggiungi_scheda_esercizio` (
    in numeroserie int,
    in numeroripetizioni int,
    in indice int,
    in nomeesercizio varchar(50),
    in nomemacchinario varchar(50),
    in cfuser char(16) )
    
BEGIN

	declare dataemissione date;
    declare exit handler for sqlexception
    BEGIN
		rollback; -- annulla la transazione
        resignal; -- ritorna il segnale al client
	END;

	if(mydb.cf_check(cfuser) is FALSE) then
		signal sqlstate '45020'set message_text= 'CODICE FISCALE ERRATO';
	END if;
    
	set transaction isolation level READ COMMITTED; -- read committed perché unrepetable read è ok in questo caso
    start transaction;
    
		select Scheda.DataEmissione into dataemissione
        from Scheda
        where Utente = cfuser and DataScad is null;

		insert `Composta` (`DataEmissioneScheda`, `NumeroSerie`, `NumeroRipetizioni`, `Indice`, `Macchinario`, `Utente`, `NomeEsercizio`)
		values (dataemissione, numeroserie, numeroripetizioni, indice, nomemacchinario, cfuser, nomeesercizio);

		COMMIT;

END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure esegui_esercizio
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `esegui_esercizio` (
	in nome varchar(50),
    in cfuser char(16) )
BEGIN

	declare dataallen date;
    declare dataemiss date;
    declare nomeeserc varchar(50);
    declare exit handler for sqlexception
    BEGIN
		rollback; -- annulla la transazione
        resignal; -- ritorna il segnale al client
	END;
    
    if(mydb.cf_check(cfuser) is FALSE) then
		signal sqlstate '45100'set message_text= 'CODICE FISCALE ERRATO';
	END if;
    
	set transaction isolation level READ UNCOMMITTED; -- read committed perché unrepetable read è ok in questo caso
    start transaction;
    
		select Scheda.DataEmissione into dataemiss
        from Scheda
        where Utente = cfuser and DataScad is null;
    
		select SessioneAllenamento.DataAllenamento into dataallen
        from SessioneAllenamento
        where cfuser = Utente and OrarioFine is null
        limit 1;

		select Esercizio.Nome into nomeeserc
        from Esercizio
        where Nome = nomeeserc;


		insert into `Contrassegna`(`Nome`, `DataAllen`, `SerieCorrente`, `Utente`, `DataEmissScheda`)
        values (nome, dataallen, 1, cfuser, dataemiss);

		COMMIT;

END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure esegui_serieeserc
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `esegui_serieeserc` (
	in cfuser char(16),
    in nome varchar(50)
    )
    
BEGIN

	declare data_allenamento date;
    declare serie_corrente int;
    declare nuova_serie int;
    declare exit handler for sqlexception
    BEGIN
		rollback; -- annulla la transazione
        resignal; -- ritorna il segnale al client
	END;
    
    if(mydb.cf_check(cfuser) is FALSE) then
		signal sqlstate '45200'set message_text= 'CODICE FISCALE ERRATO';
	END if;
    
    set transaction isolation level READ UNCOMMITTED; -- read committed perché unrepetable read è ok in questo caso
    start transaction;

    -- Trova la data di allenamento della sessione corrente
	SELECT DataAllenamento INTO data_allenamento
    FROM SessioneAllenamento
    WHERE Utente = cfuser AND OrarioFine IS NULL
    LIMIT 1;

    -- Controlla se l'utente ha una sessione di allenamento attiva
    IF data_allenamento IS NULL THEN
        SIGNAL SQLSTATE '45321'
        SET MESSAGE_TEXT = 'L''utente non ha avviato una sessione di allenamento attiva.';
    END IF;

    -- Controlla se l'esercizio esiste già in Contrassegna per la data di allenamento corrente
    SELECT SerieCorrente INTO serie_corrente
    FROM Contrassegna
    WHERE Contrassegna.Nome = nome
		AND DataAllen = data_allenamento
		AND Utente = cfuser
	LIMIT 1;

	-- set nuova_serie = serie_corrente + serie;
    
    set nuova_serie = serie_corrente + 1;
    
        -- L'esercizio esiste già in Contrassegna, quindi aggiorna il numero di serie svolte
	UPDATE Contrassegna
	SET SerieCorrente = nuova_serie
	WHERE Contrassegna.Nome = nome
		AND DataAllen = data_allenamento
        AND Utente = cfuser;
        
	COMMIT;

END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure visualizza_schedaattiva
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `visualizza_schedaattiva` (
	in cfuser char(16) )
BEGIN
	
	declare dataemi date;
    declare exit handler for sqlexception
    begin
		rollback;
		resignal;
	end;
    
    if(mydb.cf_check(cfuser) is FALSE) then
		signal sqlstate '45300'set message_text= 'CODICE FISCALE ERRATO';
	END if;
    
    set transaction isolation level READ COMMITTED; -- read committed perché unrepetable read è ok in questo caso
    start transaction;
    
		select Scheda.DataEmissione into dataemi
		from Scheda
		where Utente = cfuser and DataScad is null;
    
		if dataemi is null then
			signal sqlstate '45450' set message_text= 'nessuna scheda attiva esistente';
		end if;
    
		select DataEmissioneScheda, NumeroSerie, NumeroRipetizioni, Indice, Macchinario, NomeEsercizio
		from Composta
		where DataEmissioneScheda = dataemi and Utente = cfuser
		order by Indice;
    
		COMMIT;
    
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure visualizza_clienti
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `visualizza_clienti`( -- pt visualizza i clienti a lui associati
	in cfpt char(16) )
BEGIN

	declare ptcfcontrol char(16);
    declare exit handler for sqlexception
    BEGIN
		rollback; -- annulla la transazione
        resignal; -- ritorna il segnale al client
	END;
    
	if(mydb.cf_check(cfpt) is FALSE) then
		signal sqlstate '45039'set message_text= 'CODICE FISCALE ERRATO';
	END if;

	set transaction isolation level READ COMMITTED;
    start transaction;
    
    
		select CF, Nome, Cognome
        from Utente
        where PersonalTrainer = cfpt;
        
	COMMIT;
        
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure archivia_scheda
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `archivia_scheda` (
	in cfuser char(16) )
BEGIN

    declare ptcf char(16);
    declare datascad date;
    
    declare exit handler for sqlexception
    BEGIN
		rollback; -- annulla la transazione
        resignal; -- ritorna il segnale al client
	END;
    
    set datascad = curdate();

	IF (mydb.check_sessione(cfuser) = 1) THEN 
			signal sqlstate '45014' set message_text = 'UTENTE STA ESEGUENDO UNA SESSIONE CON LA SCHEDA CHE VUOI ARCHIVIARE: ASPETTA CHE FINISCA';
	END IF;

	set transaction isolation level READ COMMITTED; -- read committed perché unrepetable read è ok in questo caso
    start transaction;
    
    -- Controllo se l'utente è associato correttamente al personal trainer
    select PersonalTrainer.CF into ptcf
    from Utente
    join PersonalTrainer on Utente.PersonalTrainer = PersonalTrainer.CF
    where Utente.CF = cfuser;

    if ptcf is null then
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Utente selezionato non è associato a nessun personal trainer.';
    END IF;

    -- Controllo se esiste una scheda attiva per l'utente
    if not exists (
        select *
        from Scheda
        where Utente = cfuser and Tipo = 'attiva'
    ) then
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Non esiste una scheda attiva per utente selezionato.';
    END if;
    
		update Scheda
        set DataScad = datascad
        where Utente = cfuser and Tipo = 'attiva';
        
		update Scheda
        set Tipo= 'scaduta'
        where Utente = cfuser and Tipo = 'attiva';
        
        COMMIT;

END$$

DELIMITER ;

-- -----------------------------------------------------
-- function check_sessione
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE FUNCTION `check_sessione`(
	CF varchar(16) ) returns int
    
    DETERMINISTIC
BEGIN
	declare var_result INT;
	
	IF EXISTS (
		SELECT 1
		FROM SessioneAllenamento join Scheda on SessioneAllenamento.Utente = Scheda.Utente
		WHERE SessioneAllenamento.Utente = CF and Scheda.Utente = CF
			AND Tipo = 'attiva'
			AND OrarioInizio IS NOT NULL
			AND OrarioFine IS NULL
	) THEN
		SET var_result = 1; -- 1 = Sessione Iniziata ma non finita
	ELSEIF EXISTS (
		SELECT 1
		FROM SessioneAllenamento join Scheda on SessioneAllenamento.Utente = Scheda.Utente
		WHERE SessioneAllenamento.Utente = CF and Scheda.Utente = CF
			AND Tipo = 'attiva'
			AND OrarioInizio IS NOT NULL
			AND OrarioFine IS NOT NULL
	) THEN 
		SET var_result = 2; -- 2 = Sessione Iniziata e Finita
	ELSE
		SET var_result = 3; -- 3 = Sessione non iniziata
	END IF;
	RETURN var_result;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure visualizza_schedearchiviate
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `visualizza_schedearchiviate` (
    in cfuser char(16)
)
BEGIN

	declare numsched int;
    
    if(mydb.cf_check(cfuser) is FALSE) then
		signal sqlstate '45334'set message_text= 'CODICE FISCALE ERRATO';
	END if;
    
    set transaction isolation level READ COMMITTED; -- read committed perché unrepetable read è ok in questo caso
    start transaction;
    
		SELECT count(*) into numsched
        FROM Scheda
        WHERE DataScad is not NULL and Utente = cfuser;
        
        IF(numsched = 0) THEN
			signal sqlstate '45025' set message_text = 'NON HAI SCHEDE ARCHIVIATE';
        END IF;
    
		SELECT DataEmissione, DataScad
		FROM Scheda
		WHERE Utente = cfuser AND Tipo = 'scaduta';
    
		
    
		COMMIT;
    
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure visualizza_singolaarchiviata
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `visualizza_singolaarchiviata` (
	in dataemissione date,
    in cfuser char(16) )
BEGIN
    
    declare numese int;
    declare exit handler for sqlexception
    begin
		rollback;
		resignal;
	end;
    
    if(mydb.cf_check(cfuser) is FALSE) then
		signal sqlstate '45322'set message_text= 'CODICE FISCALE ERRATO';
	END if;
    
    set transaction isolation level READ COMMITTED; -- read committed perché unrepetable read è ok in questo caso
    start transaction;

		SELECT COUNT(*) INTO numese
		FROM Scheda s
		JOIN Composta c ON s.DataEmissione = c.DataEmissioneScheda AND s.Utente = c.Utente
		WHERE s.Utente = cfuser AND s.DataEmissione = dataemissione;

		IF numese = 0 THEN
			signal sqlstate '45021' set message_text = 'DATA NON NEL DATABASE';
		ELSE
			SELECT DataEmissione, NumeroSerie, NumeroRipetizioni, Indice, Macchinario, NomeEsercizio
			FROM Scheda s
			JOIN Composta c ON s.DataEmissione = c.DataEmissioneScheda AND s.Utente = c.Utente
			WHERE s.Utente = cfuser AND s.DataEmissione = dataemissione;
		END IF;
		
        COMMIT;


END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure pt_visualizzascheda
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `pt_visualizzascheda` (
	in cfpt char(16),
    in cfuser char(16) 
)
BEGIN

	DECLARE ptcfcontrol CHAR(16);
    declare tempcfpt char(16);
    declare exit handler for sqlexception
    begin
		rollback;
		resignal;
	end;
    
    if(mydb.cf_check(cfuser) is FALSE) and (mydb.cf_check(cfpt) is FALSE) then
	signal sqlstate '45039'set message_text= 'CODICE FISCALE ERRATO';
	END if;
    
    set transaction isolation level READ UNCOMMITTED; 
    start transaction;
    
    -- Verifica se l'utente è associato al personal trainer
    SELECT CF INTO ptcfcontrol 
    FROM PersonalTrainer 
    WHERE CF = cfpt;
    
    IF cfpt IS NULL THEN
        SIGNAL SQLSTATE '45243' SET MESSAGE_TEXT = 'Personal Trainer non trovato.';
    END IF;
    
    select PersonalTrainer into tempcfpt
	from Utente
    where CF= cfuser;
    
    if(tempcfpt <> cfpt) then
			signal sqlstate '45077'set message_text= 'CLIENTE NON ASSOCIATO A QUESTO PERSONAL TRAINER';
		END if;
    
    -- Verifica se per l'utente esiste una scheda attiva
    IF NOT EXISTS (
        SELECT 1
        FROM Scheda
        WHERE Utente = cfuser AND Tipo = 'attiva'
    ) THEN
        SIGNAL SQLSTATE '45355' SET MESSAGE_TEXT = 'Nessuna scheda attiva trovata collegata a questo utente.';
    END IF;
    
    -- Se entrambi i controlli sono passati, restituisci gli elementi della scheda attiva dell'utente
    SELECT DataEmissione, s.Utente , NumeroSerie, NumeroRipetizioni, Indice, Macchinario, NomeEsercizio
    FROM Scheda s join Composta c on s.Utente = c.Utente
    WHERE s.Utente = cfuser AND Tipo = 'attiva' and DataEmissioneScheda = DataEmissione
    Order by Indice;

	COMMIT;

END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure visualizza_esercizi
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `visualizza_esercizi` ()
BEGIN
	
    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
    START TRANSACTION;
    
		SELECT Nome
		FROM Esercizio;
	
    COMMIT;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure visualizza_esercizimancanti
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `visualizza_esercizimancanti` (
	in cfuser char(16)
)
BEGIN

	declare data_attuale DATE;
    declare data_scheda DATE;
    
    set data_attuale = CURDATE();
    
    if(mydb.cf_check(cfuser) is FALSE) then
	signal sqlstate '45039'set message_text= 'CODICE FISCALE ERRATO';
	END if;
    
    set transaction isolation level READ COMMITTED; 
    start transaction;
    
    -- Trova la data della scheda attiva dell'utente
    SELECT DataEmissione INTO data_scheda
    FROM Scheda
    WHERE Utente = cfuser AND Tipo = 'attiva';
    
    -- Restituisci gli esercizi mancanti
    SELECT c.Macchinario, c.NumeroSerie, c.NumeroRipetizioni, c.Indice
	FROM Composta c
	LEFT JOIN Contrassegna co ON c.Macchinario = co.Nome 
		AND co.DataAllen = data_attuale
		AND co.Utente = cfuser
	WHERE c.DataEmissioneScheda = data_scheda
		AND co.Nome IS NULL
		ORDER BY c.Indice;

	COMMIT;

END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure visualizza_seriemancanti
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `visualizza_seriemancanti` (
	IN cfuser CHAR(16),
    IN nomeese VARCHAR(50)
)
BEGIN
	DECLARE data_emissione_scheda DATE;
    DECLARE serie_corrente INT;
    DECLARE serie_totali INT;
    DECLARE serie_mancanti INT;
    
    if(mydb.cf_check(cfuser) is FALSE) then
		signal sqlstate '45569'set message_text= 'CODICE FISCALE ERRATO';
	END if;

    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
	start transaction;

    
    -- Trova la data di emissione della scheda attiva dell'utente
    SELECT DataEmissione INTO data_emissione_scheda
    FROM Scheda
    WHERE Utente = cfuser AND Tipo = 'attiva';
    
    -- Trova il valore "seriecorrente" per l'esercizio durante la sessione di allenamento corrente
    SELECT SerieCorrente
    INTO serie_corrente
    FROM Contrassegna
    WHERE Nome = nomeese
        AND DataAllen = CURDATE()
        AND Utente = cfuser;

    if(serie_corrente is null) then
		set serie_corrente = 0;
	end if;
    
    -- Trova il numero totale di serie previste per l'esercizio
    SELECT NumeroSerie INTO serie_totali
    FROM Composta
    WHERE DataEmissioneScheda = data_emissione_scheda
        AND Macchinario = nomeese;
    
    -- Calcola il numero di serie mancanti
    SET serie_mancanti = serie_totali - serie_corrente;
    
    -- Restituisce il numero di serie mancanti
    SELECT serie_mancanti AS SerieMancanti;
    
    COMMIT;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure login
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `login` (
	IN username VARCHAR(20), 
	IN varpassword VARCHAR(50), 
    OUT varrole INT)
BEGIN

	DECLARE temprole varchar(45) ;
    
	SELECT Tipo into temprole
    FROM GeneralUser
    WHERE GeneralUser.Username = username AND Pass = SHA1(varpassword)
    LIMIT 1;

    
    IF temprole = "pt" THEN
		SET varrole = 1 ;
	ELSEIF temprole = "utente" THEN
		SET varrole = 2 ;
	ELSEIF temprole = "segreteria" THEN
		SET varrole = 3 ;
	ELSE
		SET varrole = 4 ;
	END IF ;
    
    COMMIT;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure generareport
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `generareport`(
IN var_trainer_cf VARCHAR(20), 
IN var_dataInizio DATE, 
IN var_dataFine DATE)
BEGIN

	declare exit handler for sqlexception ## Dichiarazione Gestore eccezione sollevata dal Trigger
    begin
        rollback; ## Annullamento Transazione
        resignal; ## Ridirezione Segnale al Client
    end;
    
	DROP TABLE IF EXISTS report_temp;
		
	-- Crea una tabella temporanea per il report
	CREATE TEMPORARY TABLE report_temp (
	CF VARCHAR(20),
	Nome VARCHAR(45),
	Cognome VARCHAR(45),
	DataScheda DATE,
	Sessione DATE,
	Completamento VARCHAR(10),
	Durata CHAR(10));
	
        
	SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
	START TRANSACTION;
        
		-- Inserisci i dati nel report temporaneo
		INSERT INTO report_temp (CF, Nome, Cognome, DataScheda, Sessione, Completamento, Durata)
		SELECT u.CF as CF, u.Nome as Nome, u.Cognome as Cognome, sa.DataEmissione as DataScheda, ses.DataAllenamento as Sessione,
			CONCAT(IFNULL((SUM(co.SerieCorrente) / SUM(comp.NumeroSerie)) * 100, 0), '%%') AS Completamento,
			CAST(TIMEDIFF(ses.OrarioFine, ses.OrarioInizio) AS CHAR(10)) AS Durata
		FROM Utente u
			JOIN Scheda sa ON u.CF = sa.Utente
			JOIN SessioneAllenamento ses ON sa.Utente = ses.Utente 
				AND sa.DataEmissione = ses.DataEmiss
			JOIN Composta comp ON sa.Utente = comp.Utente 
				AND sa.DataEmissione = comp.DataEmissioneScheda
			LEFT JOIN Contrassegna co ON ses.Utente = co.Utente 
				AND ses.DataEmiss = comp.DataEmissioneScheda 
				AND ses.DataAllenamento = co.DataAllen 
				AND comp.Macchinario = co.Nome
		WHERE u.PersonalTrainer = var_trainer_cf and OrarioFine is not NULL
			AND ses.DataAllenamento BETWEEN var_dataInizio AND var_dataFine
		GROUP BY u.CF, u.Nome, u.Cognome, ses.DataAllenamento, sa.DataEmissione;

		-- Seleziona i dati dal report temporaneo per visualizzarli
		SELECT *
		FROM report_temp;
	
    COMMIT;

END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure cf_user
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `cf_user` (
	in username varchar(20)

)
BEGIN
	SET TRANSACTION ISOLATION LEVEL READ COMMITTED ;
	START TRANSACTION ;
    
		SELECT CF 
		FROM Utente 
		WHERE Utente.Username = username;
	
	COMMIT;

END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure cf_pt
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `cf_pt` (
	in username varchar(20)
)
BEGIN

	SET TRANSACTION ISOLATION LEVEL READ COMMITTED ;
	START TRANSACTION ;

	SELECT CF
	FROM PersonalTrainer 
	WHERE PersonalTrainer.Username = username;

	COMMIT;

END$$

DELIMITER ;
USE `mydb`;

DELIMITER $$
USE `mydb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mydb`.`GeneralUser_BEFORE_INSERT` BEFORE INSERT ON `GeneralUser` FOR EACH ROW
BEGIN
    DECLARE username_count INT;
    
    -- Controlla se l'username esiste già
    SELECT COUNT(*)
    INTO username_count
    FROM GeneralUser
    WHERE Username = NEW.Username;
    
    -- Se esiste già, ritorna un messaggio di errore
    IF username_count > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Utente già esistente';
    END IF;

END$$

USE `mydb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mydb`.`Scheda_BEFORE_INSERT` BEFORE INSERT ON `Scheda` FOR EACH ROW
BEGIN

	DECLARE count_active_schedules INT;

    -- Controlla se esiste già una scheda attiva per lo stesso utente
    SELECT COUNT(*)
    INTO count_active_schedules
    FROM Scheda
    WHERE Utente = NEW.Utente
      AND DataScad is null; -- Verifica se la data di scadenza è successiva o uguale a oggi

    -- Se esiste una scheda attiva, solleva un errore
    IF count_active_schedules > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Esiste già una scheda attiva per questo utente.';
    END IF;
END$$

USE `mydb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mydb`.`Esercizio_BEFORE_INSERT` BEFORE INSERT ON `Esercizio` FOR EACH ROW
BEGIN
	DECLARE count_existing_exercises INT;

    -- Controlla se l'esercizio è già presente nel database
    SELECT COUNT(*)
    INTO count_existing_exercises
    FROM Esercizio
    WHERE Nome = NEW.Nome;

    -- Se l'esercizio è già presente, solleva un errore
    IF count_existing_exercises > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Esercizio già presente nel database.';
    END IF;
END$$

USE `mydb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mydb`.`Composta_BEFORE_INSERT` BEFORE INSERT ON `Composta` FOR EACH ROW
BEGIN
DECLARE count_indices INT;

    -- Controlla se esiste già un esercizio con lo stesso indice per la stessa scheda
    SELECT COUNT(*)
    INTO count_indices
    FROM Composta
    WHERE DataEmissioneScheda = NEW.DataEmissioneScheda
      AND Utente = NEW.Utente
      AND Indice = NEW.Indice;

    -- Se esiste un esercizio con lo stesso indice, solleva un errore
    IF count_indices > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Esiste già un esercizio con lo stesso indice per questa scheda di esercizi.';
    END IF;
END$$

USE `mydb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mydb`.`SessioneAllenamento_BEFORE_INSERT` BEFORE INSERT ON `SessioneAllenamento` FOR EACH ROW
BEGIN
	IF HOUR(CURRENT_TIME()) < 7 OR HOUR(CURRENT_TIME()) > 23 THEN
        SIGNAL SQLSTATE '45007'
        SET MESSAGE_TEXT = 'Puoi iniziare una sessione solo tra le 7:00 e le 23:00';
    END IF;
END$$

USE `mydb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mydb`.`SessioneAllenamento_BEFORE_UPDATE` BEFORE UPDATE ON `SessioneAllenamento` FOR EACH ROW
BEGIN
	IF HOUR(CURRENT_TIME()) < 7 OR HOUR(CURRENT_TIME()) > 23 THEN
        SIGNAL SQLSTATE '45008'
        SET MESSAGE_TEXT = 'Puoi chiudere una sessione solo tra le 7:00 e le 23:00';
    END IF;
END$$

USE `mydb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mydb`.`Contrassegna_BEFORE_INSERT` BEFORE INSERT ON `Contrassegna` FOR EACH ROW
BEGIN
	DECLARE count_existing_executions INT;

    -- Controlla se l'esecuzione dell'esercizio è già presente nella sessione di allenamento
    SELECT COUNT(*)
    INTO count_existing_executions
    FROM Contrassegna
    WHERE DataAllen = NEW.DataAllen
    AND Nome = NEW.Nome;

    -- Se l'esecuzione è già presente, solleva un errore
    IF count_existing_executions > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Esecuzione dell''esercizio per questa sessione già presente nel database.';
    END IF;
END$$

USE `mydb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mydb`.`EsercizioInesist_BEFORE_INSERT_1` BEFORE INSERT ON `Contrassegna` FOR EACH ROW
BEGIN
	DECLARE esercizio_count INT;
    
    -- Controlla se l'esercizio esiste nella tabella SchedaEsercizi
    SELECT COUNT(*) INTO esercizio_count
    FROM Composta
    WHERE Utente = NEW.Utente
        AND DataEmissioneScheda = NEW.DataEmissScheda
        AND Macchinario = NEW.Nome;

    -- Se l'esercizio non esiste, genera un errore
    IF esercizio_count = 0 THEN
        SIGNAL SQLSTATE '45900'
        SET MESSAGE_TEXT = 'Esercizio non esiste nella scheda.';
    END IF;
END$$

USE `mydb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mydb`.`OrarioInser_BEFORE_INSERT_1` BEFORE INSERT ON `Contrassegna` FOR EACH ROW
BEGIN
    IF HOUR(CURRENT_TIME()) < 7 OR HOUR(CURRENT_TIME()) > 23 THEN
        SIGNAL SQLSTATE '45001'
        SET MESSAGE_TEXT = 'L\'orario di inserimento deve essere compreso tra le 7:00 e le 23:00';
    END IF;
END$$

USE `mydb`$$
CREATE DEFINER =`root`@`localhost` TRIGGER `mydb`.`Contrassegna_BEFORE_UPDATE` BEFORE UPDATE ON `Contrassegna` FOR EACH ROW
BEGIN
	DECLARE num_serie INT;
    
    -- Ottieni il numero di serie dall'esercizischeda
    SELECT NumeroSerie INTO num_serie
    FROM Composta
    WHERE new.Nome = Macchinario
    AND new.DataEmissScheda = DataEmissioneScheda
    AND new.Utente = Composta.Utente;

    -- Verifica se la serie corrente è maggiore del numero di serie
    IF new.SerieCorrente > num_serie THEN
        SIGNAL SQLSTATE '45002'
            SET MESSAGE_TEXT = 'La serie corrente è maggiore del numero di serie consentito.';
    END IF;
END$$

USE `mydb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mydb`.`OrarioUpdate_BEFORE_UPDATE_1` BEFORE UPDATE ON `Contrassegna` FOR EACH ROW
BEGIN
	IF HOUR(CURRENT_TIME()) < 7 OR HOUR(CURRENT_TIME()) > 23 THEN
        SIGNAL SQLSTATE '45003'
        SET MESSAGE_TEXT = 'L\'orario di aggiornamento deve essere compreso tra le 7:00 e le 23:00';
    END IF;
END$$


DELIMITER ;
CREATE USER 'utente' IDENTIFIED BY 'us3r!';

GRANT ALL ON TABLE `mydb`.`Utente` TO 'utente';
CREATE USER 'personaltrainer';

GRANT ALL ON TABLE `mydb`.`PersonalTrainer` TO 'personaltrainer';
CREATE USER 'segreteria';

GRANT ALL ON `mydb`.* TO 'segreteria';
GRANT ALL ON procedure `mydb`.`aggiungi_personaltrainer` TO 'segreteria';
GRANT ALL ON procedure `mydb`.`aggiungi_utente` TO 'segreteria';
GRANT ALL ON procedure `mydb`.`aggiungi_esercizio` TO 'segreteria';
CREATE USER 'generaluser';


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
