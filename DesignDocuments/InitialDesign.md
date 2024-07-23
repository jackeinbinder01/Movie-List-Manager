# Design Documents

You may have multiple design documents for this project. Place them all in this folder. File naming is up to you, but it should be clear what the document is about. At the bare minimum, you will want a pre/post UML diagram for the project.


```mermaid
classDiagram
    direction TB

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
    MBeans --> MBeansViews: uses
    MBeansLoad --> MBeansDeserializer: uses
    MBeansFormatter --> MBeansSerializer: uses
    FilterPane ..|> ActionListener : implements
    FilterPane ..> JTextField : uses
    FilterPane ..> JComboBox : uses
    FilterPane ..> JXMultiThumbSlider : uses
    FilterPane ..> JButton : uses
    FilterPane ..> GridBagConstraints : uses
    FilterPane ..> List : uses
    FilterPane ..> MBeans : uses
    FilterPane ..> JFrame : uses
    FilterPane ..> JPanel : inherits

    namespace MODEL {
        class IModel
        class Model
        class IMovieList
        class MovieData
        class MovieList
        class formatters
        class filter
        class beans
        class net
    }

    namespace VIEW {
        class IView
        class BaseView
        class FilterPane
        class ListPane
        class DetailsPane
        class JFrame

        class List
        class JPanel
        class ActionListener
        class JTextField
        class JComboBox
        class JXMultiThumbSlider
        class JButton
        class GridBagConstraints
    }

    namespace CONTROLLER {
        class IController
        class Controller
        class IFeature
    }

    namespace formatters {
        class Formats
        class MBeansDeserializer
        class MBeansSerializer
        class MBeansFormatter
        class MBeansLoader
    }

    namespace filter {
        class FilterHandler
        class FilterOperation
        class IFilterHandler
        class Operations
    }

    namespace beans {
        class MBeans
        class MBeansViews
    }

    namespace net {
        class NetUtils
    }


    class Model {

    }

    class BaseView {
        + BaseView(): void
        + display(): void
        + bindFeatures(IFeature features): void
        + setDetailsPaneEntry(MBeans record): void
        + setMainTableRecords(Stream~MBeans~ records): void
        + getFilters(): void
        + clearFilters(): void
    }


    class IModel {
        <<interface>>
        - DEFAULT_DATA
        + loadSourceData() void
        + loadWatchList() void
        + getMovieList(FilterClass, UserListIdentifier) Stream~MBeans~
        + saveWatchList(String, Stream~MBeans~) void
        + getFiltered() Stream~MBeans~
    }

    class IController {
        <<interface>>
        + go(): void
    }

    class IFeature {
        <<interface>>
        + showRecordDetails(MBeans record): void
        + addListFromFile(String fileName): void
        + exportListToFile(String fileName): void
        + applyFilters(): void
        + clearFilters(): void
    }

    class Controller {
        - model: IModel
        - view: IView
        + Controller(IModel model, IView view)
        + go(): void
        + showRecordDetails(MBeans record): void
        + addListFromFile(String fileName): void
        + exportListToFile(String fileName): void
        + applyFilters(): void
        + clearFilters(): void
    }

    class IView {
        <<interface>>
        + display(): void
        + setDetailsPane(): void
        + bindFeatures(IFeature features): void

    }

    class FilterPane {
        - ListMBeans: List
        - titleFilter: JTextField
        - contentTypeFilter: JComboBox
        - genreFilter: JComboBox
        - mpaRatingFilter: JComboBox
        - releasedFilter: JXMultiThumbSlider
        - imdbRatingFilter: JXMultiThumbSlider
        - runtimeFilter: JXMultiThumbSlider
        - boxOfficeFilter: JXMultiThumbSlider
        - directorFilter: JTextField
        - actorFilter: JTextField
        - writerFilter: JTextField
        - languageFilter: JComboBox
        - countryOfOriginFilter: JComboBox
        - applyFilterButton: JButton
        - cleaFilterButton: JButton
        - gbc: GridBagConstraints
        - filterRow: int
        + FilterPane()
        - addLabel(String filterTitle): void
        - addFilter(String filterTitle, Object filter): void
        - addButton(String buttonTitle, JButton button): void
        - configureThumbSlider(JXMultiThumbSlider thumbSlider): void
        + actionPerformed(ActionEvent e): void
        + bindFeatures(IFeature features): void
    }

    class ListPane {
        - JPanel listPanel
        - JTable listTable
        - JButton list1
        - JButton list2
        - JButton addList
        - JButton exportList
        - JButton removeCurrentEntry
        + ListPane()
        + addColumns(JTable listTable)
        + fillInTable(JTable listTable)
        + bindFeatures(IFeature features): void
    }

    class DetailsPane {
        - DateTimeFormatter DATE_FORMAT
        - int DEFAULT_WIDTH
        - int DEFAULT_HEIGHT
        - Color DEFAULT_COLOR
        - JScrollPane scrollPane
        - JPanel detailsPanel
        - JTextOabe mediaTitle
        - JLabel mediaImage
        - List~JTextArea~ mediaDetails
        - JCheckBox watchedBox
        - JTextField userRating
        - JButton saveRating
        + DetailsPane()
        - initContent() void
        - addVerticalPadding(int) void
        - addTitlePane() void
        - addImageLabel() void
        - addWatched() void
        - addDetailPane(String) void
        - addUserRating() void
        - reSize() void
        - scaleImage(URL) ImageIcon
        - setMedia(MBeans) void
        + addListeners() void
        + bindFeatures(IFeature features): void
    }


    class MBeans {
        - Title: String
        - Year: int
        - Type: String
        - Rated: String
        - Released: Date
        - Runtime: int
        - Genre: String
        - Director: List~String~
        - Writer: List~String~
        - Actors: List~String~
        - Plot: String
        - Language: List~String~
        - Country: String
        - Awards: String
        - Poster: URL
        - HasWatched: bool
        - Metascore: int
        - imdbRating: double
        - myRating: double
        - boxOffice: int
    }

    class MBeansLoader {

    }

    class MBeansFormatter {

    }
```
