# **Documentation - Demo01App**

Bienvenue dans la documentation de l'application **Demo01App**. Cette documentation regroupe toutes les informations
nécessaires pour comprendre, configurer, et étendre l'application.

## **Table des Matières**

### **1. Introduction**

- [A propos de Demo01App](00-introduction.md)

### **2. Services**

Les services sont au cœur de l'architecture de Demo01App. Voici une liste des services principaux ainsi que leur rôle :

- [ConfigurationService : Service de Configuration](03-configuration.md)
  Explique le chargement et la gestion des paramètres de l'application depuis un fichier de configuration ou la ligne de
  commande.
- [RenderingService : Service de Rendu Graphique](07-le-rendu-graphique.md)
  Détails sur la gestion de l'affichage et du rendu de la fenêtre en utilisant les configurations d'application.
- [PhysicEngineService : Service du Moteur Physique](05-le-moteur-physique.md)
  Description des fonctionnalités du moteur physique, y compris la gestion de la gravité et des collisions.
- [SceneManagerService : Gestionnaire de Scènes](09-scene-et-scene-manager.md)
  Détails concernant la navigation et la gestion des scènes comme `PlayScene`.
- [InputService : Service de Gestion des Entrées](04-entite-et-entitymanager.md)
  Prise en charge des interactions avec l'utilisateur via clavier ou autres dispositifs.

### **3. Scènes**

Les scènes définissent les éléments interactifs et les comportements dans Demo01App.

- [PlayScene : Scène de Jeu](10-scene-de-jeu.md)
  Documentation sur la scène principale du jeu `PlayScene`.

### **4. Configuration**

Documents détaillant les paramètres et options de configuration pour personnaliser l'application :

- [Configuration via ConfigurationService](docs/03-configuration.md)
  Apprenez comment adapter Demo01App via le fichier `config.properties` ou des arguments CLI.
- [Fichier de Configuration par Défaut](../main/resources/config.properties)
  Le fichier par défaut incluant les paramètres de l'application.

### **5. Exécution de l'Application**

- [Guide de lancement](../../README.md)
  Instructions pour exécuter l'application depuis un IDE ou la ligne de commande.

### **6. Développement et Contribution**

Pour ceux qui souhaitent contribuer au projet ou explorer le code source :

- [Contribution au projet](../../contribution.md)
  Règles et standards pour ajouter vos améliorations ou correctifs au projet.
- [Guide de Débogage et Test](docs/debug-and-test.md)
  Astuces pour mener des tests ou activer le mode débogage à l'exécution.

## **Liens Importants**

- [Dépôt GitHub](https://github.com/mcgivrer/ECSDemo01)
  Retrouvez le code source complet de ce projet sur GitHub.
- [Licence MIT](../../LICENSE)
  Conditions d'utilisation et de partage du projet.

### **📚 Étendez vos connaissances**

Consultez la documentation complète dans chaque fichier mentionné pour mieux comprendre l'architecture, les
fonctionnalités et les possibilités d'extension de Demo01App.
Ce fichier `index.md` regroupe toutes les pages de documentation liées au projet et leur donne un ordre logique, ce qui
facilite la navigation au sein de la documentation. Il peut être enrichi selon l'évolution du projet.
