# **Documentation du service `ConfigurationService`**

## **1. Description Générale**

Le **`ConfigurationService`** est un composant central de l'application permettant de **charger, traiter et gérer les
paramètres de configuration**. Ces paramètres peuvent provenir d’un fichier de configuration (`config.properties`) ou
être fournis via des **arguments en ligne de commande**.
L'objectif principal est d'offrir une centralisation des configurations, d'assurer un accès sécurisé et performant à ces
paramètres, et de permettre une grande flexibilité pour personnaliser le comportement de l'application, que ce soit
pour :

- Le rendu graphique,
- Les dimensions et propriétés des fenêtres,
- Les paramètres physiques,
- Les informations liées aux scènes ou aux modes de débogage.

## **2. Fonctionnalités Principales**

### 2.1 Chargement des Configurations

- Lecture des paramètres par défaut depuis un fichier de configuration (`config.properties`).
- Les clés/valeurs sont stockées dans une **structure thread-safe (ConcurrentHashMap)** pour permettre un accès rapide
  et sécurisé.

### 2.2 Décodage et Conversion des Valeurs

- Chaque configuration est traitée pour être convertie dans son type natif (`int`, `Dimension`, `Vector2d`, etc.).
- La méthode **`extractConfigValue(String key, String value)`** garantit cette interprétation, permettant aux composants
  applicatifs de récupérer directement des paramètres adaptés à leurs besoins.

### 2.3 Surcharge via Arguments CLI

- Si des arguments sont passés via la ligne de commande (par exemple `--key=value`), ils peuvent **remplacer les valeurs
  par défaut** définies dans le fichier de configuration.
- Cela permet des ajustements "à la volée" sans avoir à modifier les fichiers de configuration directement.

### 2.4 Ajustements Dynamiques

- Les valeurs de configuration sont directement utilisées par d'autres services et composants du jeu (ex.
  `RenderingService`, `SceneManagerService`).

### 2.5 Statistiques d'utilisation

- Le service maintient une trace des accès aux configurations via une statistique interne (`nbGetValues`).

## **3. Clés de Configuration Mises à Jour**

Voici la liste complète des clés de configuration disponibles dans `config.properties`, leur type attendu, ainsi que
leur rôle :

### **3.1 Paramètres de l'Application**

| **Clé**            | **Type**    | **Exemple**                | **Description**                                  |
|--------------------|-------------|----------------------------|--------------------------------------------------|
| `app.window.title` | `String`    | `Demo01App (1.0.4 Power4)` | Définit le titre de la fenêtre de l'application. |
| `app.debug.level`  | `int` (0–6) | `0` ou `5`                 | Niveau de debug (0 = aucun, 6 = très détaillé).  |

### **3.2 Configuration Graphique (Rendering)**

| **Clé**                         | **Type**    | **Exemple** | **Description**                                                        |
|---------------------------------|-------------|-------------|------------------------------------------------------------------------|
| `app.render.buffer.size`        | `Dimension` | `320x200`   | Taille du buffer de rendu interne.                                     |
| `app.render.window.size`        | `Dimension` | `640x400`   | Taille de la fenêtre d'affichage en pixels.                            |
| `app.render.window.max.buffers` | `int`       | `3`         | Nombre maximum de buffers (arrière-plans) pour le rendu de la fenêtre. |
| `app.render.frame.rate`         | `int`       | `60`        | Fréquence d'images (FPS) pour le rendu.                                |

### **3.3 Paramètres du Moteur Physique (Physics)**

| **Clé**                      | **Type**             | **Exemple**   | **Description**                                                   |
|------------------------------|----------------------|---------------|-------------------------------------------------------------------|
| `app.physic.world.gravity`   | `Vector2d`           | `0.0,0.981`   | Gravité appliquée dans le moteur physique (vecteur directionnel). |
| `app.physic.world.play.area` | `Dimension` ou autre | `560.0x320.0` | Taille de l'aire de jeu (en pixels).                              |
| `app.physics.update.rate`    | `int`                | `120`         | Fréquence d'actualisation des calculs physiques (UPS).            |

### **3.4 Gestion des Scènes**

| **Clé**              | **Type** | **Exemple**                                | **Description**                                                                    |
|----------------------|----------|--------------------------------------------|------------------------------------------------------------------------------------|
| `app.scenes.list`    | `String` | `play:com.snapgames.demo.scenes.PlayScene` | Liste des scènes disponibles avec leur classe associée sous la forme `nom:classe`. |
| `app.scenes.default` | `String` | `play`                                     | Nom de la scène par défaut au démarrage de l'application.                          |

### **3.5 Modes de Débogage et Tests**

| **Clé**             | **Type**          | **Exemple** | **Description**                                                          |
|---------------------|-------------------|-------------|--------------------------------------------------------------------------|
| `app.debug.level`   | `int`             | `0`         | Niveau d'informations de debug affichées dans la console (entre 0 et 6). |
| `app.debug.counter` | `int` (optionnel) | `10`        | Compteur de debug interne (souvent utilisé uniquement pour les tests).   |

## **4. Cas d'Utilisation Mises à Jour**

### 4.1 Initialisation par le Fichier de Configuration

- **But :** Charger les configurations globales depuis un fichier (`config.properties`).
- **Flux :**
    1. Le fichier est lu ligne par ligne.
    2. Chaque clé/valeur est extraite et convertie dans son type natif.
    3. Les configurations sont appliquées aux services et composants associés.

### 4.2 Ajustements dynamiques via CLI

- **But :** Personnaliser certains paramètres au moment de l’exécution.
- **Exemple :**

``` bash
  java -jar game.jar --app.render.window.size=1280x720 --app.physic.world.gravity=0.0,1.5
```

- Les valeurs passées sur la ligne de commande remplacent celles du fichier de configuration.

### 4.3 Paramétrage Avancé pour Tests

- **But :** Activer des tests avec des configurations spécifiques.
- **Exemple :**

``` properties
  # Test specific
  app.debug.counter=500
```

Cela limite l'exécution à 500 boucles de mise à jour pour les tests automatiques, par exemple.

## **5. Exemple de Ligne de Commande**

Un exemple combinant différentes options pour configurer l'application depuis le terminal :

``` bash
java -jar demo01app.jar \
  --app.window.title="My Custom Game" \
  --app.render.window.size=1024x768 \
  --app.render.frame.rate=45 \
  --app.physic.world.gravity=0.0,1.2
```

Ce qui effectue :

- Changement du titre de la fenêtre,
- Adaptation de la taille de la fenêtre à 1024x768,
- Réduction de la fréquence d'images à 45 FPS,
- Modification de la gravité dans la simulation physique.

## **6. Conclusion**

Les clés récemment ajoutées rendent le service **`ConfigurationService`** encore plus modulable et flexible. En gérant
des aspects clés comme le rendu, la physique, et les scènes, tout en offrant une personnalisation rapide via la CLI, il
constitue un outil puissant pour adapter dynamiquement le comportement de l'application.
