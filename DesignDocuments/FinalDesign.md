# Movie List Manager: Final Design UML Class Diagram

```mermaid
classDiagram
    direction LR

    namespace MODEL {
        class IModel
        class Model
        class IMovieList
        class MovieData
        class MovieList

    }

    namespace VIEW {
        class IView
        class BaseView
        class FilterPane
        class ListPane
        class DetailsPane
        class AppFont
    }

    namespace CONTROLLER {
        class IController
        class Controller
        class IFeature
    }

    namespace Formatters {
        class DomainXMLWrapper
        class Formats
        class MBeansDeserializer
        class MBeansSerializer
        class MBeansFormatter
        class MBeansLoader
    }

    namespace Filters {
        class FilterHandler
        class FilterOperation
        class IFilterHandler
        class Operations
    }

    namespace Movies {
        class MBeans
    }

    namespace Net {
        class NetUtils
        class APIBeans
        class MovieAPIHandler
    }
    
    namespace APP {
        class MovieListManager
    }
    
    MovieListManager --> IModel: uses
    MovieListManager --> IView: uses
    MovieListManager --> IController: uses
    
    Model ..|> IModel: implements
    BaseView ..|> IView: implements
    Controller ..|> IController: implements
    Controller ..|> IFeature: implements
    MovieList ..|> IMovieList: implements
    
    Model --> MovieData: uses
    Model --> MovieList: uses
    
    Controller --> IModel: uses
    Controller --> IView: uses
    
    BaseView --> FilterPane: uses
    BaseView --> ListPane: uses
    BaseView --> DetailsPane: uses
    BaseView --> AppFont: uses
    
    
    class MovieListManager { 
        - MovieListManager()
        - main(String[] args): void$
    }
    
    class IModel {
        + DEFAULT_DATA: String$
        + loadSourceData(): void
        + loadWatchList(String filename): int
        + createNewWatchList(String name): int
        + getRecords(): Stream~MBeans~
        + getRecords(int userListId): Stream~MBeans~
        + getRecords(List~List~String~~ filters): Stream~MBeans~
        + getRecords(int userListId, List~List~String~~ filters): Stream~MBeans~
        + saveWatchList(String filename, int userListId/*, UserListIdentifier userListId*/): void
        + addToWatchList(MBeans media, int userListId): void
        + removeFromWatchList(MBeans media, int userListId): void
        + updateWatched(MBeans media, boolean watched): void
        + updateUserRating(MBeans media, double rating): void
        + updateSourceList(): void
        + updateSourceList(Set~MBeans~ moviesToAdd): void
        + getUserListName(int userListId): String
        + getUserListCount(): int
        + getUserListIndicesForRecord(MBeans record): int[]
        + clearFilter(): void
        + addNewMBeans(List~List~String~~ filters, Stream<MBeans> movieStream): void
        + extractFilterValues(List~List~String~~ filters): Map<String, String>
        + fetchMBeans(String title, String year1, String year2): Set~<MBeans~
    }
    
    class IView {
        + setDetailsPaneEntry(MBeans record): void
        + clearTableSelection(): void
        + setSourceTableRecords(Stream~MBeans~ records, String[] watchlistNames, boolean[][] recordWatchlistMatrix): void
        + setUserTableRecords(Stream~MBeans~ records, int userListId): void
        + addUserTable(String watchlistName): void
        + bindFeatures(IFeature features): void
        + getFilterPane(): FilterPane
        + getDetailsPane(): DetailsPane
        + display(): void
        + getCurrentTab(): int
    }
    
    class IController {
        + go(): void
    }
    
    class IFeature {
        + handleTableSelection(MBeans record): void
        + importListFromFile(String filepath): void
        + exportListToFile(String filepath): void
        + removeFromWatchlist(MBeans mbean, int userListIndex): void
        + addToWatchlist(MBeans mbean, int userListIndex): void
        + createWatchlist(String name): void
        + deleteWatchlist(int userListIndex): void
        + changeRating(MBeans mbean, double rating): void
        + changeWatchedStatus(MBeans record, boolean watched, String caller): void
        + applyFilters(): void
        + clearFiltersAndReloadRecords(): void
        + handleTabChange(int tabIndex): void
    }
    
    class BaseView {
        - APP_TITLE: String$
        - DEFAULT_WIDTH: int
        - DEFAULT_HEIGHT: int
        + FilterPane
        + ListPane
        + DetailsPane
        + BaseView()
    }

    class Model {
        - sourceList: Set~MBeans~
        - watchLists: List~IMovieList~
        - filterHandler: IFilterHandler
        - filter: List~List~String~~
        + Model()
        + addNewMBeansToSource(Set~MBeans~ newMBeans): void
        + setFilter(List~List~String~~ filter): void
        + getMatchedObjectFromSource(MBeans media): MBeans
    }
    
    class Controller {
        + IModel
        + IView
        + Controller(IModel model, IView view)
        - DEV_InitModel(): void
        - getRecordsForCurrentView(): Stream~MBeans~
        - getRecordUserListMatrix(Stream<MBeans> records): boolean[][]
        - getWatchlistNames(): String[]
        - getFilterOptions(): List~List~String~~
    }
    
```