package com.programmerpro.cricketlivestreaming.data.util

object StringConstants {
    object Movie {
        const val StatusReleased = "Released"
        const val BudgetDefault = "$10M"
        const val WorldWideGrossDefault = "$20M"

        object Reviewer {
            const val FreshTomatoes = "Fresh Tomatoes"
            const val FreshTomatoesImageUrl = ""
            const val ReviewerName = "Rater"
            const val ImageUrl = ""
            const val DefaultCount = "1.8M"
            const val DefaultRating = "9.2"
        }
    }

    object Assets {
        const val Top250Movies = "movies.json"
        const val MostPopularMovies = "movies.json"
        const val InTheaters = "movies.json"
        const val MostPopularTVShows = "movies.json"
        const val MovieCategories = "movieCategories.json"
        const val MovieCast = "movieCast.json"
    }

    object Exceptions {
        const val UnknownException = "Unknown Exception!"
        const val InvalidCategoryId = "Invalid category ID!"
    }

    object Composable {
        object ContentDescription {
            fun moviePoster(movieName: String) = "Movie poster of $movieName"
            fun image(imageName: String) = "image of $imageName"
            const val MoviesCarousel = "Movies Carousel"
            const val UserAvatar = "User Profile Button"
            const val DashboardSearchButton = "Dashboard Search Button"
            const val BrandLogoImage = "Brand Logo Image"
            const val FilterSelected = "Filter Selected"
            fun reviewerName(name: String) = "$name's logo"
        }

        const val CategoryDetailsFailureSubject = "category details"
        const val MoviesFailureSubject = "movies"
        const val MovieDetailsFailureSubject = "movie details"
        const val HomeScreenTrendingTitle = "Trending"
        const val HomeScreenNowPlayingMoviesTitle = "Now Playing"
        const val PopularFilmsThisWeekTitle = "Popular this week"
        const val FilmsTitle = "Tom & Jerry"
        const val ShowsTitle = "TV Shows"
        const val SearchTitle = "Search Results"
        const val BingeWatchDramasTitle = "Popular TV Shows"
        fun movieDetailsScreenSimilarTo(name: String) = "Similar to $name"
        fun reviewCount(count: String) = "$count reviews"

        object Placeholders {
            const val AboutSectionTitle = ""
            const val AboutSectionDescription = ""
            const val AboutSectionAppVersionTitle = "Application Version"
            const val LanguageSectionTitle = "Language"
            val LanguageSectionItems = listOf(
                "English (US)",
                "English (UK)",
                "Français",
                "Española",
                "हिंदी"
            )
            const val SearchHistorySectionTitle = "Search history"
            const val SearchHistoryClearAll = "Clear All"
            val SampleSearchHistory = listOf(
                "The Light Knight",
                "Iceberg",
                "Jungle Gump",
                "The Devilfather",
                "Space Wars",
                "The Lion Queen"
            )
            const val SubtitlesSectionTitle = "Settings"
            const val SubtitlesSectionSubtitlesItem = "Subtitles"
            const val SubtitlesSectionLanguageItem = "Subtitles Language"
            const val SubtitlesSectionLanguageValue = "English"
            const val AccountsSelectionSwitchAccountsTitle = "Switch accounts"
            const val AccountsSelectionLogOut = "Log out"
            const val AccountsSelectionChangePasswordTitle = "Change password"
            const val AccountsSelectionChangePasswordValue = "••••••••••••••"
            const val AccountsSelectionAddNewAccountTitle = "Add new account"
            const val AccountsSelectionViewSubscriptionsTitle = "View subscriptions"
            const val AccountsSelectionDeleteAccountTitle = "Delete account"
            const val HelpAndSupportSectionTitle = "Help and Support"
            const val HelpAndSupportSectionListItemIconDescription = "select section"
            const val HelpAndSupportSectionFAQItem = "FAQ's"
            const val HelpAndSupportSectionPrivacyItem = "Privacy Policy"
            const val HelpAndSupportSectionContactItem = "Contact us on"
        }

        const val VideoPlayerControlPlayFullScreenButton = "FullScreen Button"
        const val VideoPlayerControlClosedCaptionsButton = "Playlist Button"
        const val VideoPlayerControlSettingsButton = "Playlist Button"
        const val VideoPlayerControlPlayPauseButton = "Playlist Button"
        const val VideoPlayerControlForward = "Fast forward 10 seconds"
    }
}
