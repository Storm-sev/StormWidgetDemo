package com.example.customnewdemo.bean
data class UserHeadBean(
    val accept_submission: Boolean,
    val articles_count: Int,
    val author: Author,
    val can_manage: Boolean,
    val column_type: String,
    val comment_permission: String,
    val created: Int,
    var description: String? = null,
    val followers: Int,
    val id: String,
    val image_url: String,
    val intro: String,
    val is_following: Boolean,
    val items_count: Int,
    val title: String,
    val topics: List<Topic>,
    val type: String,
    val updated: Int,
    val url: String,
    val url_token: String,
    val videos_count: Int,
    val voteup_count: Int
)

data class Author(
    val avatar_url: String,
    val avatar_url_template: String,
    val description: String,
    val gender: Int,
    val headline: String,
    val id: String,
    val is_advertiser: Boolean,
    val is_followed: Boolean,
    val is_following: Boolean,
    val is_org: Boolean,
    val name: String,
    val type: String,
    val uid: String,
    val url: String,
    val url_token: String,
    val user_type: String
)

data class Topic(
    val id: String,
    val name: String,
    val type: String,
    val url: String
)

