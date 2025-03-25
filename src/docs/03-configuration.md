# Service de Configuration - `ConfigurationService`

## Description Générale

La classe **`ConfigurationService`** est un service destiné à **charger, traiter et gérer les paramètres de
l'application** à partir d'un fichier de configuration ou via des arguments en ligne de commande. Les propriétés sont
interprétées et stockées dans une structure centralisée, qui permet un accès typé et sécurisé par les autres composants
de l'application.
Ce service joue un rôle central dans la personnalisation et l'adaptabilité de l'application, en facilitant la
configuration des composants tels que les dimensions de la fenêtre, les propriétés graphiques, les paramètres physiques
ou encore les niveaux de débogage.

## Fonctionnement Principal

### 1. Chargement des propriétés

- Le service charge des propriétés à partir d'un fichier de configuration au format clé/valeur (`config.properties`).
- Ces propriétés sont stockées dans une instance `Properties` qui est ensuite transférée dans une **structure
  thread-safe** (`ConcurrentHashMap`) pour des accès rapides et sécurisés.

### 2. Décodage des valeurs

- La méthode **`extractConfigValue(String key, String value)`** traite chaque clé/valeur.
- Elle interprète les valeurs en fonction de leur catégorie (texte, dimensions, vecteurs, etc.) et les convertit
  dynamiquement dans le type approprié (e.g., `int`, `boolean`, `Dimension`, `Vector2d`, `Rectangle2D`).

### 3. Application des valeurs

- Les valeurs extraites sont utilisées pour configurer différents composants de l'application :
    - Taille et propriétés de la fenêtre.
    - Options de rendu.
    - Paramètres physiques (par exemple : gravité, aire de jeu).
    - Scènes gérées par l'application.
    - Niveau de debug.

### 4. Modification via la ligne de commande

- Les arguments passés via le terminal (par exemple, `--key=value`) peuvent surcharger les valeurs du fichier de
  configuration.
- Cela permet de personnaliser rapidement certains paramètres sans modifier le fichier de configuration.

## Cas d'Utilisation (Use Cases)

### 1. Charger un fichier de configuration par défaut

- **Objectif :** Utiliser un fichier standard pour initialiser les paramètres (comme `config.properties`).
- **Étapes :**
    1. Charger les clés/valeurs du fichier.
    2. Valider chaque clé et effectuer les conversions nécessaires.
    3. Appliquer les configurations aux composants concernés (par exemple la taille de la fenêtre ou la liste des
       scènes).

### 2. Ajuster les paramètres à l'exécution

- **Objectif :** Appliquer des personnalisations en temps réel via des arguments de ligne de commande.
- **Exemple :** Surcharger la taille de la fenêtre ou activer un mode de débogage avancé :

``` bash
  java App --app.render.window.size=1280x720 --app.debug.level=5
```

### 3. Configurer le moteur physique

- **Objectif :** Initialiser les propriétés du moteur physique (gravité, aire de jeu).
- **Exemple :** Ajuster la gravité pour une simulation ou étendre la zone de jeu (e.g., en pixels).

### 4. Modifier dynamiquement les scènes de l'application

- **Objectif :** Permettre au `SceneManagerService` de charger et gérer les scènes définies dans la configuration.
- **Exemple :** Spécifier une liste de scènes et définir une scène par défaut à charger au démarrage.

## Liste des Clés de Configuration

Voici les clés documentées provenant du fichier `config.properties` et des comportements observés dans la classe
`ConfigurationService`.

### 1. Configurations Globales

| Clé                | Type     | Exemple / Valeur par défaut | Description                                                          |
|--------------------|----------|-----------------------------|----------------------------------------------------------------------|
| `app.window.title` | `String` | `"Demo01App"`               | Titre de la fenêtre de l'application.                                |
| `app.debug.level`  | `int`    | `0`                         | Niveau de débogage (de `0` à `6`, plus élevé = logs plus détaillés). |

### 2. Configurations de Rendu (Graphiques)

| Clé                             | Type        | Exemple / Valeur par défaut | Description                                   |
|---------------------------------|-------------|-----------------------------|-----------------------------------------------|
| `app.render.buffer.size`        | `Dimension` | `320x200`                   | Dimensions du **buffer de rendu** interne.    |
| `app.render.window.size`        | `Dimension` | `640x400`                   | Dimensions de la fenêtre de rendu visible.    |
| `app.render.window.max.buffers` | `int`       | `3`                         | Nombre maximum de back buffers pour le rendu. |

### 3. Configurations Physiques

| Clé                          | Type          | Exemple / Valeur par défaut | Description                                                 |
|------------------------------|---------------|-----------------------------|-------------------------------------------------------------|
| `app.physic.world.gravity`   | `Vector2d`    | `0.0,0.981`                 | Gravité appliquée dans le moteur physique (vecteur 2D).     |
| `app.physic.world.play.area` | `Rectangle2D` | `320.0x208.0`               | Aire de jeu (espace physique utilisable) définie en pixels. |

### 4. Configurations des Scènes

| Clé                  | Type     | Exemple / Valeur par défaut                 | Description                                                                              |
|----------------------|----------|---------------------------------------------|------------------------------------------------------------------------------------------|
| `app.scenes.list`    | `String` | `play:com.snapgames.demo.scenes.PlayScene;` | Liste des scènes gérées par le `SceneManagerService` avec leurs clés et implémentations. |
| `app.scenes.default` | `String` | `play`                                      | Scène par défaut activée au démarrage.                                                   |

### 5. Clés Optionnelles (Débogage et Test)

| Clé                 | Type  | Exemple / Valeur par défaut | Description                                                  |
|---------------------|-------|-----------------------------|--------------------------------------------------------------|
| `app.debug.counter` | `int` | Non spécifié (désactivé)    | Limite de boucles itératives (exclusivement pour des tests). |

## Résumé Technique

1. **Chargement des configurations** :
    - Les valeurs sont initialement extraites d'un fichier standard (`config.properties`) et placées dans une structure
      `Properties`.

2. **Décodage typé** :
    - Les clés sont interprétées et converties dans les types correspondants (par exemple, un vecteur pour
      `app.physic.world.gravity`).

3. **Accès centralisé** :
    - Les configurations sont accessibles dans une **ConcurrentHashMap**, garantissant qu'elles peuvent être
      lues/modifiées en toute sécurité.

4. **Modifications dynamiques** :
    - Les paramètres peuvent être surchargés ou ajoutés pendant l'exécution via des arguments CLI (`--key=value`).

## Exemple d'Utilisation Pratique

### Exemple de Ligne de Commande

``` bash
java App --app.window.title="SimulationApp" --app.render.window.size=800x600 --app.debug.level=2
```

### Exemple de Fichier `config.properties`

``` properties
# Exemple : Configurations globales de l'application
app.window.title=SimulationDemo
app.debug.level=3

# Configurations de rendu
app.render.buffer.size=400x300
app.render.window.size=800x600
app.render.window.max.buffers=5

# Configurations physiques
app.physic.world.gravity=0.0,1.0
app.physic.world.play.area=800.0x600.0
```

## Conclusion

La classe **`ConfigurationService`** est un service flexible et central au fonctionnement de l'application, permettant
de configurer de manière dynamique et typée les différents aspects du programme, qu'il s'agisse des dimensions de la
fenêtre, des comportements physiques ou encore du rendu graphique. Grâce à son intégration avec des fichiers de
configuration et la possibilité de surcharger à la ligne de commande, elle offre une solution robuste et modulable.
