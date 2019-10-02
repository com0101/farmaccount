package com.snc.farmaccount.helper

const val DAY_CODE = "day_code"
const val REGULAR_EVENT_TYPE_ID = 1L

const val MONTHLY_VIEW = 1
const val YEARLY_VIEW = 2
const val EVENTS_LIST_VIEW = 3
const val WEEKLY_VIEW = 4
const val DAILY_VIEW = 5
const val LAST_VIEW = 6

const val REMINDER_OFF = -1

const val ITEM_EVENT = 0
const val ITEM_EVENT_SIMPLE = 1
const val ITEM_HEADER = 2

const val DAY = 86400
const val WEEK = 604800
const val MONTH = 2592001    // exact value not taken into account, Joda is used for adding months and years
const val YEAR = 31536000

// Shared Preferences
const val WEEK_NUMBERS = "week_numbers"
const val START_WEEKLY_AT = "start_weekly_at"
const val END_WEEKLY_AT = "end_weekly_at"
const val VIBRATE = "vibrate"
const val VIEW = "view"
const val LAST_EVENT_REMINDER_MINUTES = "reminder_minutes"
const val LAST_EVENT_REMINDER_MINUTES_2 = "reminder_minutes_2"
const val LAST_EVENT_REMINDER_MINUTES_3 = "reminder_minutes_3"
const val FONT_SIZE = "font_size"
const val LIST_WIDGET_VIEW_TO_OPEN = "list_widget_view_to_open"
const val CALDAV_SYNCED_CALENDAR_IDS = "caldav_synced_calendar_ids"
const val LAST_USED_CALDAV_CALENDAR = "last_used_caldav_calendar"
const val LAST_USED_LOCAL_EVENT_TYPE_ID = "last_used_local_event_type_id"
const val DISPLAY_PAST_EVENTS = "display_past_events"
const val REPLACE_DESCRIPTION = "replace_description"
const val SHOW_GRID = "show_grid"
const val LOOP_REMINDERS = "loop_reminders"
const val DIM_PAST_EVENTS = "dim_past_events"
const val REMINDER_AUDIO_STREAM = "reminder_audio_stream"
const val USE_PREVIOUS_EVENT_REMINDERS = "use_previous_event_reminders"
const val DEFAULT_REMINDER_1 = "default_reminder_1"
const val DEFAULT_REMINDER_2 = "default_reminder_2"
const val DEFAULT_REMINDER_3 = "default_reminder_3"
const val PULL_TO_REFRESH = "pull_to_refresh"
const val LAST_VIBRATE_ON_REMINDER = "last_vibrate_on_reminder"
const val DEFAULT_START_TIME = "default_start_time"
const val DEFAULT_DURATION = "default_duration"
const val DEFAULT_EVENT_TYPE_ID = "default_event_type_id"


// font sizes
const val FONT_SIZE_MEDIUM = 1


fun getNowSeconds() = System.currentTimeMillis() / 1000L