package com.makentoshe.habrachan.application.common.arena

/**
 * Thrown to indicate that the request to cache was successful, but there are no elements in database.
 * So, we don't want to return the empty list and that return an error which contains this exception
 * */
class EmptyArenaStorageException: ArenaStorageException("Empty arena")