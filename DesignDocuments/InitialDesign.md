# Design Documents

You may have multiple design documents for this project. Place them all in this folder. File naming is up to you, but it should be clear what the document is about. At the bare minimum, you will want a pre/post UML diagram for the project.


```mermaid
classDiagram
    direction LR

    BaseView ..|> IView: implements
    BaseView --|> JFrame: extends
    BaseView --> FilterPane: uses
    BaseView --> ListPane: uses
    BaseView --> DetailsPane: uses
    FilterPane --|> JPanel: extends
    ListPane --|> JPanel: extends
    DetailsPane --|> JPanel: extends
    Controller ..|> IController: implements
    Controller --> IModel: uses
    Controller --> IView: uses
    Model ..|> IModel: implements
    
    namespace MODEL {
        class IModel
        class Model
        class MBean
    }
    
    namespace VIEW {
        class IView
        class BaseView
        class FilterPane
        class ListPane
        class DetailsPane
        class JFrame
    }
    
    namespace CONTROLLER {
        class IController
    }
    

    
    class Model {
        
    }
    
    
    class IModel {
        + getInstance(): IModel
        
    }
    
    class IController {
        + go(): void
        
    }
    
    class IView {
        + display(): void
        + setDetailsPane(): void
        
    }
    
    class FilterPane {
        + observer: Observer
        + makeFilterPane(): void
    }
    
    class ListPane {
        + observer: Observer
        + makeListPane(): void
    }
    
    class DetailsPane {
        + observer: Observer
        + makeDetailsPane(): void
        
    }
    
    class JFrameView {
        
    }
    
    class MBean {
        - Title: String
        - Year: int
        - Type: String
        - Rated: String
        - Released: int
        - Runtime: int
        - Genre: String
        - Director: String[]
        - Writer: String[]
        - Actors: String[]
        - Plot: String
        - Language: String[]
        - Country: String
        - Awards: String[]
        - Poster: URL or String
        - HasWatched: bool
        - Metascore: int
        - imdbRating: int
        - MyRating: int
        - BoxOffice: int
    }
    
    class MovieSortStrategy {
        
    }
```
