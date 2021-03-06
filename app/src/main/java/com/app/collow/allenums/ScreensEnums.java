package com.app.collow.allenums;

/**
 * Created by harmis on 28/10/16.
 */
public enum ScreensEnums {


    LOGIN(1),
    GETPRODILEPICBYEMIAL(2),
    REGISTER(3),
    FORGOT_PASSWORD(4),
    MY_ACTIVITIES(5),
    MY_POST(6),
    GET_STATES(7),
    GET_COMMUNTIES(8),
    COMMUNTIES_FEED_ACTIVITIES(9),
    GET_INTERESTS(10),
    SAVE_MORE_INFORMATION(11),
    MY_FOLLOWING_LISTING(12),
    SEARCH_BY_COMMUNITYNAME(13),
    SEARCH_BY_COMMMUNITY_LATLONG(14),
    GET_LATLONG_BY_GOOGLE(15),
    GET_COMMUNITY_DETAILS(16),
    FOLLOW_COMMUNITY(17),UNFOLLOW_COMMUNITY(80),
    CLAIM_COMMUNITY(18),
    COMMUNITY_INFORMATION_SCREEN(19), COMMMUNITY_MENU(20), MRKER_MAP_COMMUNNITY_FEED(21), GALLERY(22), CLASSIFIED(23), EVENT(24),
    NEWS(25), POLLS(26), FORMSANDDOCS(27),
    PROPERTY_TYPE_GET(28), PROPERTY_CATEGORY_GET(29),
    CREATE_CLASSIFIED(30), CONTACT_CLASSIFIED(31), IMINTERESTED_CLASSIFIED(32),
    RIGHT_NAVIGATIONS_MENU(33), NEWS_DETAILS(34), CREATE_NEWS(35),
    CREATE_FORM_AND_DOCUMENTS(36),LIKES(37),SET_AS_HOME(38),FILTER_COMMUNITY(39),
    COMMENTS_LISTING_MAIN(40),SEND_COMMENT(41),COMMENT_LIKE_DISLIKE(42),ACFOLLOWERS(43),
    APPROVE_OR_REJECT_FOLLOWERS_REQUEST(44),
    GET_MGMT_TEAM_LISTING(45),ACVIEWADMININFO(46),
    ACLISTNEWADMIN(47),ADMINLIST(48),
    MGMT_TEAM_LISTING(49),EDIT_MGMT_TEAM(50),GET_ADMIN_COMMUNITITIES_BASED_ON_TEAM(51)
    ,MESSAGES_INBOX(52),UNREAD_MESSAGE(53),VIEW(54),CREATE_GALLERY(55)
    ,ACLISTVIEWEVENT(56),ACGETEVENTS(57),ACNEWEVENT(58),ACEDITEVENT(59),
    EVENTDETAIl(60),AC_VIEW_EVENT(61),GALLERYDETAIL(62),DELETEEVENT(63),EVENT_TYPES(64),
    SUB_GALLERY(65),CREATE_POLLS(66),USER_FULL_PROFILE_VIEW(67),
    COMMUNITY_FULL_PROFILE(68),DELETE_GALLERY(69),FALG_AS_GALLERY_INAPPROPRIATE(70),
    START_DATE(71),END_DATE(72),POLLS_DETAILS_FOR_USER(73),SAVE_POLL(74),SUBMITTED_POLL(75),ADMIN_OF_POLL(76),CHAT(77),CREATECHAT(78),GIVE_VOTE(79);;


    int methodIndex = 1;

    ScreensEnums(int methodIndex) {
        this.methodIndex = methodIndex;
    }

    public int getScrenIndex() {

        return this.methodIndex;
    }


}
