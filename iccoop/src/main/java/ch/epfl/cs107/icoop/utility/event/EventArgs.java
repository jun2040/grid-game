package ch.epfl.cs107.icoop.utility.event;

/*
EventArgs implementation inspired by C# EventArgs class
https://github.com/dotnet/runtime/blob/1d1bf92fcf43aa6981804dc53c5174445069c9e4/src/libraries/System.Private.CoreLib/src/System/EventArgs.cs
*/
public class EventArgs {
    public static EventArgs EMPTY = new EventArgs();

    /**
     * event arguments which are passed on, ensuring all and only necessary information is given
     */
    public EventArgs() {
    }
}
