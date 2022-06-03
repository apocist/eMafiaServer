-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.6.10 - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL version:             7.0.0.4053
-- Date/time:                    2014-02-06 11:27:22
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET FOREIGN_KEY_CHECKS=0 */;

-- Dumping structure for table mafiamud.roles
CREATE TABLE IF NOT EXISTS `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL DEFAULT '1',
  `name` text NOT NULL,
  `setup` text,
  `affiliation` text,
  `cat1` text,
  `cat2` text,
  `onTeam` int(1) NOT NULL DEFAULT '0',
  `teamName` text,
  `teamWin` int(1) NOT NULL DEFAULT '0',
  `visibleTeam` int(1) NOT NULL DEFAULT '0',
  `chatAtNight` int(1) NOT NULL DEFAULT '0',
  `actionCat` text,
  `targetsN1` int(2) NOT NULL DEFAULT '0',
  `targetsN2` int(2) NOT NULL DEFAULT '0',
  `targetsD1` int(2) NOT NULL DEFAULT '0',
  `targetsD2` int(2) NOT NULL DEFAULT '0',
  `customScript` text,
  `onStartup` text,
  `onDayStart` text,
  `onDayTargetChoice` text,
  `onDayEnd` text,
  `onNightStart` text,
  `onNightTargetChoice` text,
  `onNightEnd` text,
  `onVisit` text,
  `onAttacked` text,
  `onLynch` text,
  `onDeath` text,
  `victoryCon` text,
  `mayGameEndCon` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;

-- Dumping data for table mafiamud.roles: ~7 rows (approximately)
DELETE FROM `roles`;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` (`id`, `version`, `name`, `setup`, `affiliation`, `cat1`, `cat2`, `onTeam`, `teamName`, `teamWin`, `visibleTeam`, `chatAtNight`, `actionCat`, `targetsN1`, `targetsN2`, `targetsD1`, `targetsD2`, `customScript`, `onStartup`, `onDayStart`, `onDayTargetChoice`, `onDayEnd`, `onNightStart`, `onNightTargetChoice`, `onNightEnd`, `onVisit`, `onAttacked`, `onLynch`, `onDeath`, `victoryCon`, `mayGameEndCon`) VALUES
	(1, 1, 'Citizen', 'CUSTOM', 'TOWN', 'CORE', 'BENIGN', 0, NULL, 1, 0, 0, 'Vest', 3, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 'var target = self.getTarget1();\r\nif(target != null){\r\n	self.text("You will don body armor tonight.");\r\n}\r\nelse{\r\n	self.text("You change your mind and will save your bulletproof vest.");\r\n}\r\n', 'if(self.isAlive()){\r\n	if(!self.hasFlag("ROLEBLOCKED")){\r\n		var target = self.getTarget1();\r\n		if(target != null){\r\n			self.setHp(self.getHp() + 1);\r\n		}\r\n	}\r\n}', NULL, NULL, NULL, NULL, 'if(!match.isAffAlive("MAFIA")){\r\n	if(!match.isCatAlive("EVIL")){\r\n		team.setVictory(true);\r\n	}\r\n}', 'if(team.getVictory()){\r\n	team.setMayGameEnd(true);\r\n}'),
	(2, 1, 'Sheriff', 'CUSTOM', 'TOWN', 'CORE', 'INVESTIGATIVE', 0, NULL, 1, 0, 0, 'Invest', 2, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 'var target = self.getTarget1();\r\nif(target != null){\r\n	self.text("You will check "+target.getName()+" tonight.");\r\n}\r\nelse{\r\n	self.text("You will not check anyone tonight.");\r\n}', 'if(self.isAlive()){\r\n	if(!self.hasFlag("ROLEBLOCKED")){\r\n		var target = self.getTarget1();\r\n		if(target != null){\r\n			self.visit(target);\r\n			if(target.getAffiliation().equals("MAFIA")){\r\n				self.text("Your target appears to be a member of the mafia!");\r\n			}\r\n			else{\r\n				self.text("Your target does not appear to be suspcious.");\r\n			}\r\n		}\r\n	}\r\n}', NULL, NULL, NULL, NULL, 'if(!match.isAffAlive("MAFIA")){\r\n	if(!match.isCatAlive("EVIL")){\r\n		team.setVictory(true);\r\n	}\r\n}', 'if(team.getVictory()){\r\n	team.setMayGameEnd(true);\r\n}'),
	(3, 1, 'Doctor', 'DEFAULT', 'TOWN', 'CORE', 'PROTECTIVE', 1, NULL, 1, 0, 0, 'Heal', 2, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 'var target = self.getTarget1();\r\nif(target != null){\r\n	self.text("You will heal "+target.getName()+" tonight.");\r\n}\r\nelse{\r\n	self.text("You will not heal anyone tonight.");\r\n}', 'if(self.isAlive()){\r\n	if(!self.hasFlag("ROLEBLOCKED")){\r\n		var target = self.getTarget1();\r\n		if(target != null){\r\n			self.visit(target);\r\n			target.setHp(target.getHp() + 1);\r\n			target.addFlag("healInform");\r\n			target.getFlag("healInform").setScriptPost("onAttacked","\\\r\n				var doc = match.getPlayer("+self.getPlayerNum()+");\\\r\n				if(doc != null){\\\r\n					doc.text(\'Your target looks to have been attacked! Atleast you arrived to perform surgery.\');\\\r\n					self.text(\'Luckly, someone came to your rescue and performed surgery!\');\\\r\n				}\\\r\n			");\r\n			target.getFlag("healInform").setScriptPre("onDayStart","\\\r\n				self.removeFlag(\'healInform\');\\\r\n			");\r\n		}\r\n	}\r\n}', NULL, NULL, NULL, NULL, 'if(!match.isAffAlive("MAFIA")){\r\n	if(!match.isCatAlive("EVIL")){\r\n		team.setVictory(true);\r\n	}\r\n}', 'if(team.getVictory()){\r\n	team.setMayGameEnd(true);\r\n}'),
	(4, 1, 'Mafioso', 'DEFAULT', 'MAFIA', 'CORE', 'KILLING', 1, NULL, 1, 1, 1, 'Kill', 2, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, 'if(self.isAlive()){\r\n	if(!team.isRoleAlive(7){\r\n		match.changePlayerRole(self.getPlayerNum(), 7)\r\n	}\r\n}', 'var target = self.getTarget1();\r\nif(target != null){\r\n	if(self.getVarInt("lastTarget") != 0){\r\n		team.pollVoteRemove("killTarget",self.getVarInt("lastTarget"));\r\n	}\r\n	team.pollVoteAdd("killTarget",target.getPlayerNum());\r\n	self.setVarInt("lastTarget",target.getPlayerNum());\r\n	team.text(self.getName()+" suggests to kill "+target.getName()+" tonight.");\r\n}\r\nelse{\r\n	if(self.getVarInt("lastTarget") != 0){\r\n		team.pollVoteRemove("killTarget",self.getVarInt("lastTarget"));\r\n	}\r\n	team.text(self.getName()+" withdraws their suggestion to kill anyone tonight.");\r\n}', NULL, NULL, NULL, NULL, NULL, 'if(!match.isAffAlive("TOWN")){\r\n	team.setVictory(true);\r\n}', 'if(team.getVictory()){\r\n	team.setMayGameEnd(true);\r\n}'),
	(5, 1, 'Electromaniac', 'CUSTOM', 'NEUTRAL', 'KILLING', NULL, 0, NULL, 0, 0, 0, 'Charge', 2, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 'var target = self.getTarget1();\r\nif(target != null){\r\n	self.text("You will charge "+target.getName()+" tonight.");\r\n}\r\nelse{\r\n	self.text("You will not charge anyone tonight.");\r\n}', 'if(self.isAlive()){\r\n	if(!self.hasFlag("ROLEBLOCKED")){\r\n		var target = self.getTarget1();\r\n		if(target != null){\r\n			self.visit(target);\r\n			target.addFlag("CHARGED");\r\n			target.getFlag("CHARGED").setScriptPre("onVisit","\\\r\n				if(visitor.hasFlag(\'CHARGED\')){\\\r\n					match.attack(visitor, \'Electocuted\', \'been shocked to death\');\\\r\n					match.attack(self, \'Electocuted\', \'been shocked to death\');\\\r\n				}\r\n			");\r\n		}\r\n	}\r\n}', NULL, NULL, NULL, NULL, NULL, NULL),
	(6, 1, 'Escort', 'DEFAULT', 'TOWN', 'PROTECTIVE', NULL, 1, NULL, 1, 0, 0, 'Roleblock', 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 'var target = self.getTarget1();\r\nif(target != null){\r\n	self.text("You will role-block "+target.getName()+" tonight.");\r\n}\r\nelse{\r\n	self.text("You will not check anyone tonight.");\r\n}', 'if(self.isAlive()){\r\n	if(!self.hasFlag("ROLEBLOCKED")){\r\n		var target = self.getTarget1();\r\n		if(target != null){\r\n			self.visit(target);\r\n			target.addFlag("ROLEBLOCKED");\r\n			target.getFlag("ROLEBLOCKED").setScriptPre("onDayStart","\\\r\n				self.removeFlag(\'ROLEBLOCKED\');\\\r\n			");\r\n			target.text("You have been role-blocked!");\r\n		}\r\n	}\r\n}', NULL, NULL, NULL, NULL, 'if(!match.isAffAlive("MAFIA")){\r\n	if(!match.isCatAlive("EVIL")){\r\n		team.setVictory(true);\r\n	}\r\n}', 'if(team.getVictory()){\r\n	team.setMayGameEnd(true);\r\n}'),
	(7, 1, 'Godfather', 'DEFAULT', 'MAFIA', 'CORE', 'KILLING', 1, NULL, 1, 1, 1, 'Kill', 0, 0, 0, 0, NULL, '//The backslash needs to used quite alot because Strings cannot normally be split on multiple lines of code\r\nvar killTargetScript ="\\\r\n	if(self.isAlive()){\\\r\n		if(!self.hasFlag(\'ROLEBLOCKED\')){\\\r\n			var target = self.getTarget1();\\\r\n			if(target != null){\\\r\n				self.visit(target);\\\r\n				self.attack(target,\'Shot\', \'shot between the eyes\');\\\r\n			}\\\r\n		}\\\r\n	}";\r\nvar teamNightEndScript = "\\n\\\r\n	var killer;\\n\\\r\n	var numMafiaioso = team.numRoleAlive(4);\\n\\\r\n	var mafiaosos = new Array();\\n\\\r\n	var loop = 0;\\n\\\r\n	for(player in team.getAliveTeammates()){\\n\\\r\n		if(player.getRoleId() == 4){\\n\\\r\n			player.clearTargets();\\n\\\r\n			mafiaosos[loop] = player;\\n\\\r\n			loop++;\\n\\\r\n		}\\n\\\r\n		if(player.getRoleId() == 7){\\n\\\r\n			player.clearTargets();\\n\\\r\n			killer = player;\\n\\\r\n		}\\n\\\r\n	}\\n\\\r\n	var targetNum = team.getVarInt(\'GFKillTarget\');\\n\\\r\n	if(targetNum == 0){\\n\\\r\n		targetNum = team.pollHighestVote(\'killTarget\');\\n\\\r\n	}\\n\\\r\n	if(targetNum > 0){\\n\\\r\n		if(numMafiaioso > 0){\\n\\\r\n			killer = match.randomPlayer(mafiaosos);\\n\\\r\n		}\\n\\\r\n		if(killer != null){\\\r\n			team.text(killer.getName()+\' has been sent to kill \'+match.getPlayer(targetNum).getName()+\'.\');\\n\\\r\n			killer.setTarget1(targetNum);\\n\\\r\n			killer.addFlag(\'killTarget\');\\n\\\r\n			killer.getFlag(\'killTarget\').setScriptPost(\'onNightEnd\',\\""+killTargetScript+"\\");\\n\\\r\n		}\\n\\\r\n	}\\n\\\r\n	team.pollClear();\\n\\\r\n	team.setVarInt(\'GFKillTarget\',0);";\r\nteam.setScript("onNightEnd", teamNightEndScript );', NULL, NULL, NULL, NULL, 'var target = self.getTarget1();\r\nif(target != null){\r\n	team.text(self.getName()+" decides to kill "+target.getName()+" tonight.");\r\n	team.setVarInt("GFKillTarget",target.getPlayerNum());\r\n}\r\nelse{\r\n	self.text(self.getName()+" withdraws the decision to kill anyone tonight.");\r\n	team.setVarInt("GFKillTarget",0);\r\n}', NULL, NULL, NULL, NULL, NULL, 'if(!match.isAffAlive("TOWN")){\r\n	team.setVictory(true);\r\n}', 'if(team.getVictory()){\r\n	team.setMayGameEnd(true);\r\n}');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;


-- Dumping structure for table mafiamud.user_account
CREATE TABLE IF NOT EXISTS `user_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` text NOT NULL,
  `pass` text CHARACTER SET latin7 COLLATE latin7_general_cs NOT NULL,
  `pass2` text CHARACTER SET latin7 COLLATE latin7_general_cs NOT NULL,
  `reg_time` int(11) NOT NULL,
  `usergroup` int(11) NOT NULL DEFAULT '5',
  `forumid` int(11) DEFAULT '0',
  `forumjoindate` int(11) DEFAULT NULL,
  `avatarurl` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

-- Dumping data for table mafiamud.user_account: ~1 rows (approximately)
DELETE FROM `user_account`;
/*!40000 ALTER TABLE `user_account` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_account` ENABLE KEYS */;


-- Dumping structure for table mafiamud.user_groups
CREATE TABLE IF NOT EXISTS `user_groups` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `hexcolor` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table mafiamud.user_groups: ~5 rows (approximately)
DELETE FROM `user_groups`;
/*!40000 ALTER TABLE `user_groups` DISABLE KEYS */;
INSERT INTO `user_groups` (`id`, `name`, `hexcolor`) VALUES
	(0, 'Banned', '000000'),
	(2, 'Restricted', '888888'),
	(5, 'Member', 'FFFFFF'),
	(10, 'Moderator', '0000FF'),
	(15, 'Administrator', 'FF0000');
/*!40000 ALTER TABLE `user_groups` ENABLE KEYS */;


-- Dumping structure for table mafiamud.user_preregister
CREATE TABLE IF NOT EXISTS `user_preregister` (
  `username` varchar(25) NOT NULL,
  `user_group` int(11) NOT NULL DEFAULT '5',
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table mafiamud.user_preregister: ~0 rows (approximately)
DELETE FROM `user_preregister`;
/*!40000 ALTER TABLE `user_preregister` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_preregister` ENABLE KEYS */;


-- Dumping structure for table mafiamud.user_spam
CREATE TABLE IF NOT EXISTS `user_spam` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table mafiamud.user_spam: ~0 rows (approximately)
DELETE FROM `user_spam`;
/*!40000 ALTER TABLE `user_spam` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_spam` ENABLE KEYS */;


-- Dumping structure for table mafiamud.user_verify
CREATE TABLE IF NOT EXISTS `user_verify` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` text NOT NULL,
  `pass` text CHARACTER SET latin7 COLLATE latin7_general_cs NOT NULL,
  `pass2` text CHARACTER SET latin7 COLLATE latin7_general_cs NOT NULL,
  `token` text CHARACTER SET latin7 COLLATE latin7_general_cs NOT NULL,
  `reg_time` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table mafiamud.user_verify: ~0 rows (approximately)
DELETE FROM `user_verify`;
/*!40000 ALTER TABLE `user_verify` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_verify` ENABLE KEYS */;
/*!40014 SET FOREIGN_KEY_CHECKS=1 */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
